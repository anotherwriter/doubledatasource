package com.example.demo.dao;

import com.example.demo.model.db.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ResourceDao {

    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(3000);
//        httpRequestFactory.setConnectTimeout(3000);
//        httpRequestFactory.setReadTimeout(3000);
//        restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate = new RestTemplate();
    }


    public List<Resource> getSmallResourcesByIds(List<Long> resourceIds, String targetUrl){
        log.info("real get resources by ids resourceIds = {}", resourceIds);
        targetUrl = getTargetUrl(resourceIds, targetUrl);

        List<Resource> resources = null;

        try {
            log.debug("getResources from url: {}", targetUrl);

            ObjectMapper objectMapper = new ObjectMapper();
            //set request body
            Map requestMap = new HashMap();
            requestMap.put("id", resourceIds);
            System.out.println("request param:" + objectMapper.writeValueAsString(requestMap));

            String body = "{\"id\":[18722,251]}";
            //set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            //send request and parse result
            HttpResponse rsp = restTemplate.postForObject(targetUrl, entity, HttpResponse.class);
//            HttpResponse rsp = restTemplate.exchange(targetUrl, HttpMethod.POST, entity, HttpResponse.class).getBody();
//            HttpResponse rsp = restTemplate.getForEntity(targetUrl, HttpResponse.class).getBody();

            if (rsp != null && rsp.getData() != null && !rsp.getData().isEmpty()) {
                resources = rsp.getData();

                return resources;
            } else {
                log.error("wrong resource to movie format for external response: {}", rsp);
            }
        } catch (Throwable e) {
            log.error("exception when getResources from server", e);
        }

        return resources;
    }


    private String getTargetUrl(String targetUrl){
        targetUrl = String.format("http://%s", targetUrl);
        log.info("generate get resources url:{}", targetUrl);

        return targetUrl;
    }

    private String getTargetUrl(List<Long> resourceIds, String targetUrl){
        targetUrl = String.format("http://%s", targetUrl);
        log.info("generate get resources url");

        if(null == resourceIds || resourceIds.isEmpty()){
            log.info("real get resources by ids,resourceIds is null");
            return null;
        }
        targetUrl = targetUrl + "?id[]=" + resourceIds.get(0);
        for (int i = 1; i < resourceIds.size(); i++) {
            targetUrl += "&id[]=" + resourceIds.get(i);
        }

        return targetUrl;
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    private static class HttpResponse{

        private int code = 0;

        private String message = "ok";

        private List<Resource> data;
    }
}