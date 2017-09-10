package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by donghao04 on 2017/7/28.
 */
@Data
@NoArgsConstructor
@ToString
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class MovieFlow {

    private long id;

    private long movieId;

    private MovieFlowType type;

    private String playUrl;

    private String fullUrl;

    private long fileSize;

    private int fileNum;

    private String fileHash;

    private int width;

    private int height;

    private int rate;

}
