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
public class UserInfo {
    private int infoId;
    private String info;
}
