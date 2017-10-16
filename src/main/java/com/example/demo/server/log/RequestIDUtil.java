package com.example.demo.server.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestIDUtil {
    public static String getRequestId(HttpServletRequest request, HttpServletResponse response, String exceptionRequestId) {
        if (exceptionRequestId != null)
            return exceptionRequestId;
        else if (request.getHeader(RequestIdFilter.X_REQUEST_ID) != null)
            return request.getHeader(RequestIdFilter.X_REQUEST_ID);
        else if (response.containsHeader(RequestIdFilter.X_REQUEST_ID))
            return response.getHeader(RequestIdFilter.X_REQUEST_ID);
        return  null;
    }

}
