package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by donghao04 on 2017/7/28.
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class Resource  {

    private long id;

    private String md5;

    private String name;

    private String desc;

    private String fullDesc;

    private MovieType type;

    public int getResourceType() {
        return type.getCode();
    }

    private String category;

    private String []tag;

    private String []smallThumbnails;

    private String []bigThumbnails;

    private int remarks;

    private String score;

    private int playCount;

    private int duration;

    private int year;

    private String area;

    private String actor;

    private long sortId = 0;

    private Integer videoCount;

    private Integer videoCountTotal;

    private Boolean ifNew;

    private String shareLink;

    private String shareText;

    private String url;

    private Long resourceType;

    @JsonProperty("ft")
    private List<MovieFlow> movieFlows;

    @JsonIgnore
    private String pageTypeString;

    private int supportShare;

    private int supportDownload;

    private int videoFrom;

    private String brandType;

    @JsonProperty("pageType")
    public String getPageTypeDisplayName() {
        return pageTypeString;
    }

    @JsonProperty("pageType")
    public void setMoviePageType(String pageTypeStr) {
        pageTypeString = pageTypeStr;
    }

    @JsonProperty("brandType")
    public String getBrandType() {
        // for brand itself, hould be id when type is BRAND/IP/SELF
        if (type != null) {
            if (type.equals(MovieType.BRAND) || type.equals(MovieType.IP) || type.equals(MovieType.SELF)) {
                return String.valueOf(id);
            }
        }
        // for movie, should be null when brandStatus is not 1 or brandType is not BRAND
        return brandType;
    }



}
