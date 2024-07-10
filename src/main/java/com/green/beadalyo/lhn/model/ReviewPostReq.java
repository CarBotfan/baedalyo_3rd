package com.green.beadalyo.lhn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReviewPostReq {
    @JsonIgnore
    private long reviewPk;
    private long doneOrderPk;
    @JsonIgnore
    private long userPk;
    private long resPk;
    private String reviewContents;
    private int reviewRating;
    @JsonIgnore
    private String reviewPics1;
    @JsonIgnore
    private String reviewPics2;
    @JsonIgnore
    private String reviewPics3;
    @JsonIgnore
    private String reviewPics4;



}
