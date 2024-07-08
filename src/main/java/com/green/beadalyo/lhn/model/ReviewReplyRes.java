package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReplyRes {
    private long reviewPk;
    private long reviewCommentPk;
    private String commentContents;
    private long userPk;
}
