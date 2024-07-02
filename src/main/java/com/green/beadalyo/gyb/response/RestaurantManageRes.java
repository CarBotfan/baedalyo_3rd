package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    }
}
