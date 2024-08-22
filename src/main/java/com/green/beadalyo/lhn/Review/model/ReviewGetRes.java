package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.lhn.Review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private List<String> pics;

    @Schema(example = "1",description = "리뷰 상태")
    private int reviewState;

    @Schema(description = "리뷰 생성 일자")
    private String createdAt;

    @Schema(description = "리뷰 수정 일자")
    private LocalDateTime updatedAt;

    @Schema(example = "감사" ,description = "사장님의 리뷰에 대한 답변")
    private ReviewReplyRes reply;

    @Schema(example = "본인 신고여부" ,description = "")
    private Integer reviewReportState;

    @Schema(example = "리뷰 블라인드 여부" ,description = "")
    private Integer reviewBlind = 0;



    private String resName;

    public ReviewGetRes(Review review) {
        this.reviewPk = review.getReviewPk();
        this.userPk = review.getUserPk().getUserPk();
        this.nickName = review.getUserPk().getUserNickname();
        this.reviewContents = review.getReviewContents();
        this.reviewRating = review.getReviewRating();
        this.createdAt = review.getCreatedAt().toString();
        this.updatedAt = review.getUpdatedAt();
        this.pics = new ArrayList<>();
        if (review.getReviewPics1() != null) pics.add(review.getReviewPics1());
        if (review.getReviewPics2() != null) pics.add(review.getReviewPics2());
        if (review.getReviewPics3() != null) pics.add(review.getReviewPics3());
        if (review.getReviewPics4() != null) pics.add(review.getReviewPics4());
        this.resName = review.getResPk().getName();
        this.reviewBlind = review.getReviewBlind();
    }
}
