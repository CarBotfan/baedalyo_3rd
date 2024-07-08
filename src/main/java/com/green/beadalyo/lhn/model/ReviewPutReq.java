package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewPutReq {
    private long userPk;
    private long reviewPk;
    private String reviewContents;
    private int reviewRating;
    private MultipartFile[] files;
    private List<String> removePics;
    private int reviewState;


}
