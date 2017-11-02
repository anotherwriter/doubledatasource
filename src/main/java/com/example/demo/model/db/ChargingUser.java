package com.example.demo.model.db;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChargingUser {
    private long id;

    private String xuid; // device xuid

    private String md5; // md5sum xuid

    private String phone; // user's phone

    private int chargeStatus; // 0:default value, just record user req 1:send charging req to operator 2:charging successful
}
