package com.example.demo.listener;

import com.example.demo.model.db.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MyInterceptor1 implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(">>>MyInterceptor1-preHandle(): " + request.getMethod()
                + " " + request.getRequestURI());
//        if(MyApplicationListener.isClosed  && handler instanceof HandlerMethod){
//            System.out.println(">>>MyInterceptor1-preHandle(): deny this request." );
//            PrintWriter out = response.getWriter();
//            ObjectMapper objectMapper = new ObjectMapper();
//            out.print(objectMapper.writeValueAsString(new ApiResponse<>()));// back json data
//            out.flush();
//            return false;
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(">>>MyInterceptor1-postHandle(): " + request.getMethod());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println(">>>MyInterceptor1-afterCompletion(): " + request.getMethod());
    }
}
