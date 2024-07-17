package com.green.beadalyo.lhn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewReplyReq {

//    @Schema(description = "리뷰 댓글의 고유 pk", example = "1")
    @JsonIgnore
    private long reviewCommentPk;

    @Schema(description = "리뷰의 고유 pk", example = "123")
    private long reviewPk;

    @Schema(description = "댓글 내용", example = "감사")
    private String commentContent;
}