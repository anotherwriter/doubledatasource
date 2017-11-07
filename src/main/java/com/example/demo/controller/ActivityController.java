package com.example.demo.controller;

import com.example.db2.LastRunTimeMapper;
import com.example.demo.dao.ChargingUserDao;
import com.example.demo.model.db.ApiResponse;
import com.example.demo.model.db.ChargingDataReq;
import com.example.demo.model.db.ChargingUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/api/activity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ActivityController {

    @Value("${activity.xiaomi.charging.enable:true}")
    private boolean xiaomiChargingDataEnabled;

    @Value("${activity.operator.callback.enable:true}")
    private boolean operatorCallBackEnabled;

    private final ChargingUserDao chargingUserDao;

    private final LastRunTimeMapper lastRunTimeMapper;


    @RequestMapping(method = RequestMethod.GET, value = "/send_req")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse sendReqToOpeartor() {
        log.info("send req to operator.");

        List<ChargingUser> chargingUsers = chargingUserDao.getChargingUserByStatus(0);

        String workerUrl = "http://localhost:8080/api/activity/HandleSubmit/submit";
        SubmitReq submitReq = new SubmitReq();

        ObjectMapper mapper = new ObjectMapper();
        String body = "param=";
        try {
            body += mapper.writeValueAsString(submitReq);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("send req data: {}", body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate httpClient = new RestTemplate();
        String result = httpClient.postForObject(workerUrl, httpEntity, String.class);

        SubmitRsp submitRsp = new SubmitRsp();
        return new ApiResponse(submitRsp);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/HandleSubmit/submit")
    @ResponseStatus(HttpStatus.OK)
    public String simulateOpeartor(@RequestParam(name = "param") String jsonStr) {
        log.info("submit req. param={}", jsonStr);

        ObjectMapper objectMapper = new ObjectMapper();
        SubmitReq submitReq = null;
        try {
            submitReq = objectMapper.readValue(jsonStr, SubmitReq.class);
        } catch (IOException e) {
            log.error("operator callBack exception={}", e);
        }

        log.debug("submitReq={}", submitReq);

        SubmitRsp submitRsp = new SubmitRsp();
        submitRsp.setCode("0");
        submitRsp.setDescription("success");
        String rspStr = null;
        try {
            rspStr = objectMapper.writeValueAsString(submitRsp);
        } catch (JsonProcessingException e) {
            rspStr = "{\"code\":500,\"message\":\"internal error\"}";
            e.printStackTrace();
        }
        return rspStr;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/charging_data/is_new_user")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse isNewUser(@RequestBody @Valid ChargingDataReq req) {
        log.info("is new user. req={}", req);
        if (!isNewUser(req.getXuid())) {
            return new ApiResponse(-100, "not new user");
        }

        return ApiResponse.EmptyResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/charging_data/do_charging")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse doCharging(@RequestBody @Valid ChargingDataReq req) {
        log.info("doCharging req. req={}", req);
        if (!xiaomiChargingDataEnabled) {
            return new ApiResponse(-400, "this activity is closed");
        }

        if (!isNewUser(req.getXuid())) {
            return new ApiResponse(-100, "not new user");
        }

        if (!isLegalPhone(req.getPhone())) {
            return new ApiResponse(-200, "form error");
        }

        if (!checkRedeem(req.getXuid(), req.getPhone())) {
            return new ApiResponse(-300, "had already redeemed");
        }

        ChargingUser chargingUser = new ChargingUser();
        chargingUser.setXuid(req.getXuid());
        chargingUser.setMd5(MD5(req.getXuid()));
        chargingUser.setPhone(req.getPhone());

        int rows = chargingUserDao.insertChargingUser(chargingUser);
        if (0 == rows) {
            log.error("already have this data");
        }
        log.info("insertChargingUser finish. chargingUser={}, rows={}", chargingUser, rows);

        return ApiResponse.EmptyResponse;
    }

    /**
     *
     * @param state 0: success 1: fail 2: unknown
     * @return 2: charge success 3: charge failed 4: charge unknown'
     */
    private int callBackState2ChargeStatus(String state) {
        int stateIntValue = 2; // initiate unknown
        int chargeStatus = 4; // initiate unknown

        try {
            stateIntValue = Integer.valueOf(state);
        } catch (Exception e) {
            log.error("callBackState2Status state={}, exception={}", state, e);
            return 4;
        }

        if (0 == stateIntValue) {
            chargeStatus = 2; // charge successfully
        } else if (1 == stateIntValue) {
            chargeStatus = 3; // charge failed
        } else { // statIntValue equals 2 or other value
            chargeStatus = 4; // unknown
        }

        return chargeStatus;
    }

    /**
     * check if the user had already redeemed
     * @param xuid
     * @param phone
     * @return
     */
    private boolean checkRedeem(String xuid, String phone) {
        String md5 = MD5(xuid);
        List<ChargingUser> chargingUsers = chargingUserDao.getChargingUserByPhoneOrMd5(phone, md5);

        if (null == chargingUsers || 0 == chargingUsers.size()) {
            log.debug("xuid or phone is new. md5={}", md5);
            return true;
        }

        log.error("xuid or phone is not new. md5={} phone={}", md5, phone);
        return false;
    }

    /**
     * only check if the user is old user.
     * @param xuid
     * @return
     */
    private boolean isNewUser(String xuid) {
        // check xuid form
        if (StringUtils.isEmpty(xuid)) {
            log.error("xuid is empty");
            return false;
        }

        if (!xuid.contains("CUID") || !xuid.contains("MACID")) {
            log.error("xuid form error");
            return false;
        }

        // search md5sum(xuid) from last_run_time
        String md5 = MD5(xuid);
        int count = lastRunTimeMapper.getMd5Num(md5);

        log.debug("count={}, md5={}", count, md5);
        if (count != 0) {
            return false;
        }

        return true;
    }

    /**
     * only check the phone's form
     * @param phone
     * @return
     */
    private boolean isLegalPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            log.error("phone is empty");
            return false;
        }

        final Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.find()) {
            log.error("form error. phone: {}", phone);
            return false;
        }

        return true;
    }

    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashText = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
