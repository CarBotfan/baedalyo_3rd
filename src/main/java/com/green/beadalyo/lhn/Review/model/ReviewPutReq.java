package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewPutReq {


    @Schema(example = "123",description = "리뷰의 고유 pk")
    private long reviewPk;

    @Schema(example = "음식이 아주 맛있었어요!", description = "리뷰 내용")
    private String reviewContents;

    @Schema(example = "5", description = "리뷰 평점 (1~5)")
    private int reviewRating;

    @Schema(description = "리뷰 이미지 파일 경로 목록")
    private List<String> files;

    @Schema(description = "제거할 리뷰 이미지 파일 경로 목록")
    private List<String> removePics;

    @Schema(example = "1",description = "리뷰 상태")
    private int reviewState;
}