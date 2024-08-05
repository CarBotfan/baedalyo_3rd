package com.green.beadalyo.lhn.coupon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponResponseDto {

    // 쿠폰의 고유 ID
    private Long id;

    // 쿠폰 이름
    private String name;

    // 쿠폰 내용
    private String content;

    // 쿠폰 할인 금액 (고정 금액)
    private Long price;

    // 쿠폰 생성 일시
    private LocalDateTime createdAt;

    public CouponResponseDto(Long id, String name, String content, Long price, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.price = price;
        this.createdAt = createdAt;
    }
}