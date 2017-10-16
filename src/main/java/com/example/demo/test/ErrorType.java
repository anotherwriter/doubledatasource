package com.example.demo.test;

import org.springframework.util.StringUtils;

import java.util.EnumSet;
import java.util.Set;

public enum ErrorType {
    OK(0, "successful"),
    ERROR_UID(-1, "error uid");



    ErrorType(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    private final Integer errorCode;

    private final String errorMsg;

    public static String getErrorMsgByErrorCode(Integer code) {
        for (ErrorType errorType : ErrorType.values()) {
            if (errorType.errorCode.equals(code)) {
                return errorType.getErrorMsg();
            }
        }
        return "unknown error!";
    }


    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

