package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewPostRes {

    private long reviewPk;
    private long doneOrderPk;
    private long userPk;
    private long resPk;
    private String reviewContents;
    private MultipartFile[] files;
    private int reviewRating;



}
