package com.green.beadalyo.lhn.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponUserResponseDto {
    private Long id;
    private Long couponId;
    private String userId;
    private int state;
    private LocalDateTime createdAt;
}