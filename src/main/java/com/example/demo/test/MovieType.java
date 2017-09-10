package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by donghao04 on 2017/7/28.
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MovieType implements CodeEnum {
    UNKNOWN(0),
    NORMAL_2D(1), NORMAL_3D_LR(2), NORMAL_3D_TB(3),
    FULL_2D(4), FULL_3D_LR(5), FULL_3D_TB(6),
    XBASE_URL_NORMAL(7), XBASE_URL_ZHIBO(8), XBASE_URL_MOVIE_NORMAL(9), XBASE_URL_MOVIE_FULL(10), XBASE_URL_GALLERY(11),

    // bellow is group entity which can be refered by pageType field
    BRAND(20),
    IP(21),
    SELF(22),


    // resource in App table
    APP(100),

    // no real movie in Movie table
    FEED_DAOHANG(101),
    FEED_WAILIAN(102),
    FEED_ACTIVITY(103),
    FEED_AD(104),
    FEED_LIST(105);

    @Getter
    public final int code;
}