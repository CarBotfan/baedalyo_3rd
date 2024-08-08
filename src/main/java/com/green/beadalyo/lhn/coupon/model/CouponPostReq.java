package com.green.beadalyo.lhn.coupon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.gyb.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data

public class CouponPostReq {
    @Schema(description = "가격", example = "1000")
    private Integer price;
    @Schema(description = "쿠폰 내용", example = "짱할인")
    private String content;
    @Schema(description = "쿠폰 이름", example = "오픈기념할인쿠폰")
    private String name;
    @Schema(description = "쿠폰 최소 주문금액", example = "10000")
    private Long minOrderAmount;
    @JsonIgnore
    private Restaurant restaurant;
}
