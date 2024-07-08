package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReviewPostReq {

    private long reviewPk;
    private long doneOrderPk;
    private long userPk;
    private long resPk;
    private String reviewContents;
    private int reviewRating;
    private String reviewPics1;
    private String reviewPics2;
    private String reviewPics3;
    private String reviewPics4;



}
