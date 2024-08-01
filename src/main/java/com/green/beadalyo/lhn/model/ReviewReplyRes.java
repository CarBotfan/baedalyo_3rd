package com.green.beadalyo.lhn.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReplyRes {

    @Schema(description = "리뷰의 고유 pk", example = "123")
    private long reviewPk;

    @Schema(description = "리뷰 댓글의 고유 pk", example = "1")
    private long reviewCommentPk;

    @Schema(description = "댓글 내용", example = "감사")
    private String commentContents;

}