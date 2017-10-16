package com.example.demo.server.log;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class RequestIdFilter implements Filter {

    Logger log = LoggerFactory.getLogger(getClass());

    ThreadLocal<Long> beginTime = new ThreadLocal<>();

    ThreadLocal<String> threadLocalRequestId = new ThreadLocal<>();

    public static final String X_REQUEST_ID = "X-Request-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException, ServletException {
        preHandle((HttpServletRequest) request, (HttpServletResponse) response);
        chain.doFilter(request, response);
        afterCompletion((HttpServletRequest) request, (HttpServletResponse) response);
    }

    private void preHandle(HttpServletRequest request, HttpServletResponse response) {
        // get or create requestId
        String requestId = request.getHeader(X_REQUEST_ID);
        if (requestId == null) {
            requestId = response.getHeader(X_REQUEST_ID);
            if (requestId != null)
                log.debug("X_REQUEST_ID found in HttpServletResponse");
        }
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
            log.info("X_REQUEST_ID not found in header, generate requestId:{} ", requestId);
        }

        threadLocalRequestId.set(requestId);
        response.addHeader(X_REQUEST_ID, requestId);
        beginTime.set(System.currentTimeMillis());
        log.info("[begin] {} {} {}", request.getMethod(), request.getRequestURI(), charReader(request));
    }

    private void afterCompletion(HttpServletRequest request, HttpServletResponse response) {
        String requestId = threadLocalRequestId.get();
        String responseRequestId = response.getHeader(X_REQUEST_ID);
        if (!requestId.equals(responseRequestId)) {
            log.error("response requestId changed. requestId={}, responseRequestId={}", requestId, responseRequestId);
        }
        response.setHeader(X_REQUEST_ID, requestId);
        long timeUsed = System.currentTimeMillis() - beginTime.get();
        log.info("[status:{},time:{}ms] {} {}", response.getStatus(), timeUsed, request.getMethod(),
                request.getRequestURI());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

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
}

