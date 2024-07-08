package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ReviewGetRes {
    private long reviewPk;
    private long userPk;
    private String reviewContents;
    private int reviewRating;
    private List<String> pics;
    private int reviewState;
    private String createdAt;
    private String updatedAt;
    private ReviewReplyRes reply;
}
