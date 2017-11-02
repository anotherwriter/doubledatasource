package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperatorCallBack {
    private String partyId;
    private CallBackData data;
    private String time;
    private String sign;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CallBackData {
        private int type;
        private int size;
        private List<CallBackMessage> messageList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CallBackMessage {
        private String state;
        private String mobiles;
        private String userPackage;
        private String recvTime;
        private String sendID;
        private String statedes;
        private String requestid;
    }
}
