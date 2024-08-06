package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewReplyUpdReq {
    @Schema(description = "리뷰 댓글의 고유 pk", example = "1")
    private long reviewCommentPk;

    @Schema(description = "댓글 내용", example = "감사")
    private String commentContent;
}
