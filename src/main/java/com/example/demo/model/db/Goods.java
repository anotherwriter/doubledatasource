package com.example.demo.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private int id;
    private String goodsName;
    private String goodsDesc;
    private int userId;
}
