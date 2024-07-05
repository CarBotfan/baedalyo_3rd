package com.green.beadalyo.lhn.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReviewReplyReq {
    private long reviewCommentPk;
    private long reviewPk;
    private String commentContent;
}
