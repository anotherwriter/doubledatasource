package com.example.demo.controller;

import lombok.Data;

import java.util.List;

@Data
public class SubmitReq {

    private Header header;
    private Body body;

    @Data
    public static class Header {
        private String sign; // md5("body" + body + "key" + key + "partyId" + partyId + "requestid" + requestid
        private long partyId; // 云漫分配
    }

    @Data
    public static class Body {
        private List<UserData> userdataList;
        private int size; // userdataList size
        private String type; // 0
        private String requestid; // 时间戳(精确到毫秒 17位) + 用户名 + 4位随机数字(1-9999自增循环使用 不足4位补零)
    }

    @Data
    public static class UserData {
        private String userPackage; // 流量包大小 30M -> 30
        private String mobiles;
    }
}


