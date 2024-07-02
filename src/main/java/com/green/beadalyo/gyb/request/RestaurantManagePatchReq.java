package com.green.beadalyo.gyb.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RestaurantManagePatchReq
{
    @Schema(description = "개점 시간")
    private String openTime ;

    @Schema(description = "폐점 시간")
    private String closeTime ;

    @Schema(description = "주소")
    private String addr ;

    @Schema(description = "위도")
    private Double coorX ;

    @Schema(description = "경도")
    private Double coorY ;

    @Schema(description = "음식점 이름")
    private String restaurantName ;

    @Schema(description = "사업자 번호")
    private String regiNum ;

}
