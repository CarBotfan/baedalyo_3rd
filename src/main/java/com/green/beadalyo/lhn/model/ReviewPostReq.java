package com.green.beadalyo.lhn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewPostReq {
    @JsonIgnore
    @Schema(description = "리뷰의 고유 pk")
    private long reviewPk;

    @Schema(example = "1", description = "완료된 주문의 고유 Pk")
    private long doneOrderPk;

    @JsonIgnore
    @Schema(example = "3",description = "사용자의 고유 Pk")
    private long userPk;

//    @Schema(example = "2", description = "식당의 고유 Pk")
//    private long resPk;

    @Schema(example = "음식이 아주 맛있었어요!", description = "리뷰 내용")
    private String reviewContents;

    @Schema(example = "5", description = "리뷰 평점 (1~5)")
    private int reviewRating;

    @JsonIgnore
    @Schema(description = "리뷰 이미지 파일 경로 1")
    private String reviewPics1;

    @JsonIgnore
    @Schema(description = "리뷰 이미지 파일 경로 2")
    private String reviewPics2;

    @JsonIgnore
    @Schema(description = "리뷰 이미지 파일 경로 3")
    private String reviewPics3;

    @JsonIgnore
    @Schema(description = "리뷰 이미지 파일 경로 4")
    private String reviewPics4;
}