package com.example.demo.test;


import com.example.demo.controller.SubmitReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.net.util.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.EnumSet.of;

/**
 * Created by donghao04 on 2017/7/28.
 */

public class Main {


    public static void getClassification(CommonInterface commonInterface) {
        System.out.println("test print classification: " + commonInterface.getClassification());
    }


    public static void main(String[] args) {

        String workerUrl = "http://localhost:8080/api/activity/HandleSubmit/submit";
        SubmitReq submitReq = new SubmitReq();

        ObjectMapper mapper = new ObjectMapper();
        String body = "param=";
        try {
            body += mapper.writeValueAsString(submitReq);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("send req data: " + body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate httpClient = new RestTemplate();
        String result = httpClient.postForObject(workerUrl, httpEntity, String.class);

        System.out.println("rsp result=" + result);


        String bduss = "2c2cjZHYk5OTWFrOTBmM3JaWG50WjB1dEVCUUwtMDBzSWNnTHZkNnVHcnFtQ0phSVFB\n" +
                "QUFBJCQAAAAAAAAAAAEAAACt~giVdnJfZWR1Y2F0aW9uAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOoL-1nqC~tZY";

        int salt = 0;
        for (int i = 0; i < bduss.length(); ++i) {
            salt += i;
            salt += (byte) bduss.charAt(i);
        }
        salt = salt % 2 + 1;
        String str = bduss.substring(bduss.length() - salt) + bduss.substring(0, bduss.length() - salt);
        str = StringUtils.replace(str, "-", "+");
        str = StringUtils.replace(str, "~", "/");
        try {
            byte[] decoded_bytes = Base64.decodeBase64(str.getBytes("UTF-8"));
            byte[] userIdHigherBytes = new byte[4];
            byte[] userIdLowerBytes = new byte[4];
            byte[] userNameBytes = new byte[64];
            System.arraycopy(decoded_bytes, 60, userIdHigherBytes, 0, 4);
            System.arraycopy(decoded_bytes, 68, userIdLowerBytes, 0, 4);
            int userNameLen = 64;
            if (decoded_bytes.length - 8 - 72 < 64) {
                userNameLen = decoded_bytes.length - 8 - 72;
            }
            System.arraycopy(decoded_bytes, 72, userNameBytes, 0, userNameLen);
            int userIdLower = bytes2Int(userIdLowerBytes, ByteOrder.LITTLE_ENDIAN);
            int userIdHigher = bytes2Int(userIdHigherBytes, ByteOrder.LITTLE_ENDIAN);
            long userId = ((long) userIdLower & 0xFFFFFFFFl) | (((long) userIdHigher << 32) & 0x7FFFFFFF00000000l);
            System.out.println("userId: " + userId);
            String userName = new String(userNameBytes, "GB2312");
            System.out.println("userName: " + StringUtils.trimTrailingCharacter(userName, '\0'));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }



        String phone = "15675512071";
        final Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.find()) {
            System.out.println("error. phone:" + phone);
        } else {
            System.out.println("right.");
        }

        String param = "{\n" +
                "    \"partyId\": \" 1 2 0 9 9 9 2 \",\n" +
                "    \"data\": {\n" +
                "        \"type\": 3,\n" +
                "        \"size\": 1,\n" +
                "        \"messageList\": [\n" +
                "            {\n" +
                "                \"state\": \"0\",\n" +
                "                \"mobiles\": \"13910567310\",\n" +
                "                \"userPackage\": \"000DADSASD00B10\",\n" +
                "                \"recvTime\": \"20160302150711\",\n" +
                "                \"sendID\": \"DA8887DADADA\",\n" +
                "                \"statedes\": \"订购成功\",\n" +
                "                \"requestid\": \"sadasaaaaada\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"time\": \" 2 0 1 5 0 8 0 4 1 1 1 2 5 2 9 3 6 \",\n" +
                "    \"sign\": \" a 7 5 7 1 7 d a 5 4 2 0 4 4 7 a 4 7 c 1 6 a 2 f e 1d622ba\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        OperatorCallBack operatorCallBack = null;
        try {
            operatorCallBack = objectMapper.readValue(param, OperatorCallBack.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(operatorCallBack);

        System.out.println(new Date(1509336000002L));

        System.getProperty("https.protocols");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1509379200 * 1000L);
        System.out.println(calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        date.setTime(1509379200);
        System.out.println(df.format(date));

//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN",String.class);
//        System.out.println(result);
        final Pattern pattern1 = Pattern.compile("^heichaapp.*=(\\d+)$");

        Matcher matcher1 = pattern1.matcher("heichaapp://historyfeed?type=3");
        if (matcher1.find()) {
            System.out.println("cotent: " + matcher1.group(1));
        } else {
            System.out.println("doesn't match");
        }


    }

    private static int bytes2Int(byte[] src, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getInt();
    }

    @Data
    @ToString
    class TestData implements CommonInterface {

        private String name;

        private String gender;

        private String classification;

        private int age;

        public void setDbAge(int age) {
            this.age = age;
        }

        public int getDbAge() {
            return this.age;
        }
    }

    interface CommonInterface {
        String getClassification();
    }
}
