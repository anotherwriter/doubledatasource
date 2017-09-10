package com.example.demo.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by donghao04 on 2017/7/28.
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String userName;
    private String nikename;

    private UserInfo userInfo;
}
