package com.example.demo.test;

import java.util.EnumSet;
import java.util.Set;

public enum ClientPageType {

    SEARCH(EnumSet.of(MovieType.NORMAL_2D, MovieType.NORMAL_3D_LR, MovieType.NORMAL_3D_TB, MovieType.FULL_2D,
            MovieType.FULL_3D_LR, MovieType.FULL_3D_TB, MovieType.XBASE_URL_NORMAL, MovieType.XBASE_URL_ZHIBO,
            MovieType.XBASE_URL_MOVIE_NORMAL, MovieType.XBASE_URL_MOVIE_FULL, MovieType.XBASE_URL_GALLERY),
            "create_time desc"),

    TAB_3D(EnumSet.of(MovieType.NORMAL_2D, MovieType.NORMAL_3D_LR, MovieType.NORMAL_3D_TB), "score desc, create_time desc"),

    TAB_VR(EnumSet.of(MovieType.FULL_2D, MovieType.FULL_3D_LR, MovieType.FULL_3D_TB), "score desc, create_time desc"),

    IDX_ZB(EnumSet.of(MovieType.XBASE_URL_ZHIBO), "update_time desc"),

    IDX_QJ(EnumSet.of(MovieType.XBASE_URL_GALLERY), "create_time desc"),

    IDX_DP(EnumSet.of(MovieType.XBASE_URL_MOVIE_NORMAL), "update_time desc");

    ClientPageType(Set<MovieType> movieTypes, String orderByString) {
        this.movieTypes = movieTypes;
        this.orderByString = orderByString;
    }

    private final Set<MovieType> movieTypes;

    private final String orderByString;

    public Set<MovieType> getMovieTypes() {
        return movieTypes;
    }

    public String getOrderByString() {
        return orderByString;
    }
}
