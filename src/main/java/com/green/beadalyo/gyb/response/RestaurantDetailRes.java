package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.RestaurantDetailView;
import com.green.beadalyo.kdh.menu.model.GetAllMenuRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantDetailRes
{
    @Schema(description = "음식점 고유 번호")
    private Long restaurantPk ;
    @Schema(description = "음식점 이름")
    private String restaurantName ;
    @Schema(description = "음식점 리뷰 평균점수")
    private Double reviewScore ;
    @Schema(description = "음식점 설명")
    private String restaurantDesc ;
    @Schema(description = "리뷰 공지")
    private String reviewDesc ;
    @Schema(description = "음식점 메뉴 토탈 수")
    private Integer totalMenu ;
    @Schema(description = "음식점 총 리뷰 수")
    private Integer reviewTotalElements ;
    @Schema(description = "음식점 주소")
    private String restaurantAddr ;
    @Schema(description = "사업자 등록 번호")
    private String regiNum ;
    @Schema(description = "개점 시간")
    private String openTime ;
    @Schema(description = "폐점 시간")
    private String closeTime ;
    @Schema(description = "음식점 사진")
    private String restaurantPic ;
    @Schema(description = "음식점 상태(1 : 영업 중 / 2 : 휴점 중 / 3 : 폐업)")
    private Integer restaurantState ;
    private List<GetAllMenuRes> menuList ;

    public RestaurantDetailRes(RestaurantDetailView data)
    {
        this.restaurantPk = data.getRestaurantPk() ;
        this.restaurantName = data.getRestaurantName() ;
        this.restaurantAddr = data.getRestaurantAddr() ;
        this.regiNum = data.getRegiNum() ;
        this.restaurantDesc = data.getRestaurantDesc() ;
        this.reviewDesc = data.getReviewDesc() ;
        this.restaurantState = data.getRestaurantState() ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if (data.getOpenTime() != null)
            this.openTime = formatter.format(data.getOpenTime()) ;
        if (data.getCloseTime() != null)
            this.closeTime = formatter.format(data.getCloseTime()) ;
        this.reviewTotalElements = data.getReviewTotalElements() ;
        this.reviewScore = data.getReviewScore() ;
    }


}
