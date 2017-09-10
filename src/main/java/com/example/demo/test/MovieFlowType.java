package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by donghao04 on 2017/7/28.
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MovieFlowType implements CodeEnum {
    LOW(0), MEDIUM(1), HIGH(2), SUPER(3);

    @Getter
    public final int code;
}

