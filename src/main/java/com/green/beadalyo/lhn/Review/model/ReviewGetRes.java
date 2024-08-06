package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.lhn.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class ReviewGetRes {
    @Schema(description = "리뷰의 고유 pk")
    private long reviewPk;

    @Schema(example = "3",description = "사용자의 고유 Pk")
    private long userPk;

    @Schema(example = "닉네임",description = "리뷰 작성자의 닉네임")
    private String nickName;

    @Schema(example = "음식이 아주 맛있었어요!", description = "리뷰 내용")
    private String reviewContents;

    @Schema(example = "5", description = "리뷰 평점 (1~5)")
    private int reviewRating;


    @Schema(description = "리뷰 이미지 파일 경로 목록")
    private List<String> pics = new ArrayList<>();

    @Schema(example = "1",description = "리뷰 상태")
    private int reviewState;

    @Schema(description = "리뷰 생성 일자")
    private LocalDateTime createdAt;

    @Schema(description = "리뷰 수정 일자")
    private LocalDateTime updatedAt;

    @Schema(example = "감사" ,description = "사장님의 리뷰에 대한 답변")
    private ReviewReplyRes reply;

    @JsonIgnore
    private String reviewPics1;

    @JsonIgnore
    private String reviewPics2;

    @JsonIgnore
    private String reviewPics3;

    @JsonIgnore
    private String reviewPics4;

    private String resName;

    public ReviewGetRes(Review review) {
        this.reviewPk = review.getReviewPk();
        this.userPk = review.getUserPk().getUserPk();
        this.nickName = review.getUserPk().getUserNickname();
        this.reviewContents = review.getReviewContents();
        this.reviewRating = review.getReviewRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
        this.reviewPics1 = review.getReviewPics1();
        this.reviewPics2 = review.getReviewPics2();
        this.reviewPics3 = review.getReviewPics3();
        this.reviewPics4 = review.getReviewPics4();
        this.resName = review.getResPk().getName();
    }
}
