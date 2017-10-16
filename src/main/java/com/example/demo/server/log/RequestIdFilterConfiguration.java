package com.example.demo.server.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnExpression("${logging.has_request-id:true}")
public class RequestIdFilterConfiguration {

    @Value("${logging.requestId_urlPattern:/*}")
    private String urlPattern;

    @Bean
    public FilterRegistrationBean bceRequestIdFilterRegistrationBean() {
        RequestIdFilter filter = new RequestIdFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        List<String> urlPatterns = Arrays.asList(urlPattern.split(";"));
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
