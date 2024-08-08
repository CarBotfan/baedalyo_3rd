package com.green.beadalyo.lhn.coupon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Data;

@Data

public class CouponPostReq {

    private Integer price;
    private String content;
    private String name;
    private Long minOrderAmount;
    @JsonIgnore
    private Restaurant restaurant;
}
