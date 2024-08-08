package com.green.beadalyo.lhn.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserResponseDto {
    private Long id;
    private Long couponId;
    private String content;
    private Integer price;
    private Long minOrderAmount;
    private String name;
}