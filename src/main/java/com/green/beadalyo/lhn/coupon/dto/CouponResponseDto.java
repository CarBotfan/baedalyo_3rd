package com.green.beadalyo.lhn.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponResponseDto {

    // 쿠폰의 고유 ID
    private Long id;

    // 쿠폰 이름
    private String name;

    // 쿠폰 내용
    private String content;

    // 쿠폰 할인 금액 (고정 금액)
    private Integer price;

    // 쿠폰 생성 일시
    private LocalDateTime createdAt;

    //쿠폰 최소주문금액
    private Long minOrderAmount;

    public CouponResponseDto(Long id, String name, String content, Integer price, Long minOrderAmount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.price = price;
        this.createdAt = createdAt;
        this.minOrderAmount = minOrderAmount;
    }
}