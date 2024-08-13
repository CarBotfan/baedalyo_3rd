package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.common.ResultPage;
import com.green.beadalyo.gyb.model.RestaurantListView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantListRes
{
    @Schema(description = "가게 고유번호")
    private Long restaurantPk ;

    @Schema(description = "가게 이름")
    private String restaurantName ;

    @Schema(description = "평균 별점")
    private Float reviewAvgScore ;

    @Schema(description = "리뷰 수")
    private Integer reviewTotalElements ;

    @Schema(description = "가게 주소")
    private String restaurantAddr ;

    @Schema(description = "가게 상태")
    private Integer restaurantState ;

    @Schema(description = "팔로우 상태")
    private Integer isFollow;

    @Schema(description = "가게 사진")
    private String restaurantPic ;

    @Schema(description = "상점 쿠폰 여부")
    private Integer isCoupon;

    @Schema(description = "쿠폰 최대")
    private Integer maxCoupon;

    public RestaurantListRes(RestaurantListView view)
    {
        this.restaurantPk = view.getRestaurantPk() ;
        this.restaurantName = view.getRestaurantName() ;
        this.reviewAvgScore = view.getReviewAvgScore() ;
        this.reviewTotalElements = view.getReviewTotalElements() ;
        this.restaurantAddr = view.getRestaurantAddr() ;
        this.restaurantState = view.getRestaurantState() ;
        this.restaurantPic = view.getRestaurantPic() ;
        this.isFollow = view.getIsFollow() ;
        this.isCoupon = view.getIsCoupon();
        this.maxCoupon = view.getMaxPrice();
    }

    public static ResultPage<RestaurantListRes> toResultPage(Page<RestaurantListView> page)
    {
        if (page == null) {
            return new ResultPage<>(Page.empty());
        }
        List<RestaurantListRes> list = new ArrayList<>() ;
        for (RestaurantListView view : page)
        {list.add(new RestaurantListRes(view));}

        PageImpl<RestaurantListRes> pageImpl = new PageImpl<>(list) ;
        return new ResultPage<>(pageImpl) ;
    }

}
