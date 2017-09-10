package com.example.demo.model.db;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Created by donghao04 on 2017/7/28.
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.STRING)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Resource {

    @JsonProperty("brand_id")
    private Long brandId;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("cover")
    private String brandCover;

    @JsonProperty("thumbnail")
    private String brandThumbnail;

    @JsonProperty("full_desc")
    private String brandFullDesc;

    private long rid;//resource table id

    private long id;// old movie id or brand id

    private String md5;

    private String name;

    private String desc;

    private String fullDesc;

    private String category;

    private String[]tag;

    private String[]smallThumbnails;

    private String[]bigThumbnails;

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


    @JsonIgnore
    private String pageTypeString;

    private long pageTypeDb;

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
        return brandType;
    }

    @JsonIgnore
    public void setDbSmallThumbnails(String dbSmallThumbnails) {
        smallThumbnails = dbSmallThumbnails.split(",");
    }


    @JsonIgnore
    public void setDbBigThumbnails(String dbBigThumbnails) {
        bigThumbnails = dbBigThumbnails.split(",");
    }


    @JsonIgnore
    public void setDbTag(String dbTag) {
        tag = dbTag.split(",");
    }



}
