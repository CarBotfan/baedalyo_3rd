package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantManageRes
{
    @Schema(description = "개점 시간")
    private String openTime ;

    @Schema(description = "폐점 시간")
    private String closeTime ;

    @Schema(description = "주소")
    private String addr ;

    @Schema(description = "상호 명")
    private String restaurantName ;

    @Schema(description = "사업자 번호")
    private String regiNum ;

    @Schema(description = "가게 설명")
    private String restaurantDescription ;

    @Schema(description = "리뷰 설명")
    private String reviewDescription ;

    @Schema(description = "영업 상태 1 : 영업중 / 2 : 휴업중")
    private Integer restaurantState ;

    @Schema(description = "음식점 사진 링크")
    private String restaurantPic ;

    @Schema(description = "카테고리 리스트")
    private List<CategoryRes> categories ;


    public RestaurantManageRes(Restaurant data)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime openTimeData = data.getOpenTime();
        this.openTime = (openTimeData != null) ? openTimeData.format(formatter) : "";
        LocalTime closeTimeData = data.getCloseTime();
        this.closeTime = (closeTimeData != null) ? closeTimeData.format(formatter) : "";
        this.addr = data.getAddress();
        this.restaurantName = data.getName();
        this.regiNum = data.getRegiNum();
        this.restaurantDescription = data.getRestaurantDescription() ;
        this.reviewDescription = data.getReviewDescription() ;
        this.restaurantState = data.getState() ;
        this.restaurantPic = data.getPic();
        List<Category> categories = data.getCategories();
        List<CategoryRes> res = new ArrayList<>();
        if (categories != null) {
            categories.forEach(category -> res.add(new CategoryRes(category)));
        }
        this.categories = res ;

    }
}
