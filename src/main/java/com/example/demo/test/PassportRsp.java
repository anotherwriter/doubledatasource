package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Data
@ToString
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportRsp {

    @JsonIgnore
    private int status = 0;

    @JsonIgnore
    private Integer need_reset_cookie;

    @JsonIgnore
    private String session_id;

    @JsonIgnore
    private Long uid;

    @JsonIgnore
    private Long created_time;

    @JsonIgnore
    private Long last_login_time;

    @JsonIgnore
    private Integer access_count;

    @JsonIgnore
    private Long last_updated_time;

    @JsonIgnore
    private Long global_access_time;

    @JsonIgnore
    private Long private_access_time;

    @JsonIgnore
    private Integer pwd_flag;

    @JsonIgnore
    private Long reserved;

    @JsonProperty("mail")
    private String secureemail;

    @JsonIgnore
    private String securemobil;

    @JsonIgnore
    private Long risk_rank;

    @JsonIgnore
    private Long risk_code;

    private String username;

    @JsonIgnore
    private String displayname;

    @JsonIgnore
    private String global_data;

    @JsonIgnore
    private String private_data;

    public String getUsername() {
        try {
            if (!StringUtils.isEmpty(username)) {
                username = URLDecoder.decode(username, "gb2312");
            } else {
                username = "";
            }

        } catch (UnsupportedEncodingException e) {
            log.error("userName decoder error. userName={}, exception={}", username, e);
            return "";
        }

        return username;
    }

}
