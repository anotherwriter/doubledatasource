package com.example.demo.controller;

import com.example.demo.model.db.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Map;

@RestController
@RequestMapping(value = "/other")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OtherLog {

    @RequestMapping(method = RequestMethod.POST, value = "/test1")
    @ResponseStatus(HttpStatus.OK)
    public String testReq1(@RequestParam(name = "a") String a) {
        log.info("testPost: a={}", a);
        return "a: " + a;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test2")
    @ResponseStatus(HttpStatus.OK)
    public String testReq2(@RequestBody TestReq testReq) {
        log.info("testPost: testReq={}, params={}", testReq);
        return "testReq: " + testReq + " params:";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test3")
    @ResponseStatus(HttpStatus.OK)
    public String testReq3(@RequestParam String a, @RequestParam Map<String, String> params, HttpServletRequest request) {

        String body = charReader(request);
        log.info("testPost:  a={}, params={}, requestBody={}", a, params, body);
        return  "a:" + a +" params:" + params + " requestBody:" + body;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test4")
    @ResponseStatus(HttpStatus.OK)
    public String testReq4(HttpServletRequest request) {

        String body = charReader(request);
        log.info("testPost: requestBody={}", body);
        return   "requestBody:" + body;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test5")
    @ResponseStatus(HttpStatus.OK)
    public String testReq5(@CookieValue(value = "BDUSS", required = false) String bduss,
                           @CookieValue(value = "JSESSIONID") String jSessionId){
        log.info("BDUSS={}, JSESSIONID={}", bduss, jSessionId);
        return "JSESSIONID=" + jSessionId + " BUDSS=" + bduss;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test6")
    @ResponseStatus(HttpStatus.OK)
    public String testReq6(@CookieValue(value = "BDUSS", required = false) String bduss,
                           HttpServletRequest request){

        return getIpAddress(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test7")
    @ResponseStatus(HttpStatus.OK)
    public String testReq7(@RequestParam(value = "a", defaultValue = "hhh") String a){

        return a;
    }



    @RequestMapping(method = RequestMethod.GET, value = "{str}")
    @ResponseStatus(HttpStatus.OK)
    public String getUser(@PathVariable("str") String s) {

        return "input: " + s;
    }

    /**
     * get request body
     *
     * @param request
     * @return
     */
    String charReader(HttpServletRequest request) {
        String wholeStr = "";
        try {
            InputStream is = request.getInputStream();
            wholeStr = IOUtils.toString(is, "utf-8");
            log.error("if inputstream support:{}" ,is.markSupported());
            if (is.markSupported())
                is.reset();
        } catch (Exception e) {
            log.error("RequestIdFilter: get req body error");
            e.printStackTrace();
            return "get req body error";
        }

        return wholeStr;

    }


    /**
     * get client real ip address.
     * agent software will influence the rightness of request.getRemoteAddr()
     * @param request
     * @return
     */
    String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ip = inetAddress.getHostAddress();
            } catch (Exception e) {
                log.error("InetAddress.getLocalHost() Exception={}", e);
            }
        }
        return ip;
    }

}
