package com.green.beadalyo.lhn.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ReviewGetRes {
    @Schema(description = "리뷰의 고유 pk")
    private long reviewPk;

    @Schema(example = "3",description = "사용자의 고유 Pk")
    private long userPk;

    @Schema(example = "음식이 아주 맛있었어요!", description = "리뷰 내용")
    private String reviewContents;

    @Schema(example = "5", description = "리뷰 평점 (1~5)")
    private int reviewRating;


    @Schema(description = "리뷰 이미지 파일 경로 목록")
    private List<String> pics;

    @Schema(example = "1",description = "리뷰 상태")
    private int reviewState;

    @Schema(description = "리뷰 생성 일자")
    private String createdAt;

    @Schema(description = "리뷰 수정 일자")
    private String updatedAt;

    @Schema(example = "감사" ,description = "사장님의 리뷰에 대한 답변")
    private ReviewReplyRes reply;
}
