package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    @Schema(description = "음식점 총 리뷰 수")
    private Integer reviewTotalElements ;
    @Schema(description = "음식점 주소")
    private String restaurantAddr ;
    @Schema(description = "사업자 등록 번호")
    private String regiNum ;
    @Schema(description = "음식점 사진")
    private String restaurantPic ;
    @Schema(description = "음식점 상태(1 : 영업 중 / 2 : 휴점 중 / 3 : 폐업)")
    private Integer restaurantState ;
    private List<String> menuList ;

    public RestaurantDetailRes(Restaurant data)
    {
        this.restaurantPk = data.getSeq() ;
        this.restaurantName = data.getName() ;
        this.restaurantAddr = data.getAddress() ;
        this.regiNum = data.getRegiNum() ;
        this.restaurantDesc = data.getDescription() ;
        this.restaurantState = data.getState() ;
        //임시 데이터
        this.reviewTotalElements = (int)(Math.random()*2000) ;
        this.reviewScore = (Math.random()*5) ;
        this.menuList = new ArrayList<String>();
    }


}
