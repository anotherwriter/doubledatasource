package com.example.demo.server.exception;

import com.example.demo.server.log.RequestIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestExceptionResponse> handle(HttpServletRequest request, HttpServletResponse response, Exception exception){
        log.error("[exception:UnknownException] " + request.getMethod() + " " + request.getRequestURI(), exception);
        RestExceptionResponse body = new RestExceptionResponse();

        String requestId = RequestIDUtil.getRequestId(request, response, null);
        body.setRequestId(requestId);
        body.setCode(500);
        body.setMessage("Internal Server Error");
        log.info("[exception:{}] {} {}", body.getCode(), request.getMethod(), request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
