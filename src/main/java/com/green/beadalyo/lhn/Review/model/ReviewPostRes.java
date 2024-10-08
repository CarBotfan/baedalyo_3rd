package com.green.beadalyo.lhn.Review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewPostRes {

    @Schema(description = "리뷰의 고유 pk")
    private long reviewPk;
    @Schema(example = "1", description = "완료된 주문의 고유 Pk")
    private long doneOrderPk;
    @Schema(example = "3",description = "사용자의 고유 Pk")
    private long userPk;
    @Schema(example = "2", description = "식당의 고유 Pk")
    private long resPk;
    @Schema(example = "음식이 아주 맛있었어요!", description = "리뷰 내용")
    private String reviewContents;
    @Schema(description = "리뷰 이미지 파일 경로 목록")
    private List<String> files;
    @Schema(example = "5", description = "리뷰 평점 (1~5)")
    private int reviewRating;



}
