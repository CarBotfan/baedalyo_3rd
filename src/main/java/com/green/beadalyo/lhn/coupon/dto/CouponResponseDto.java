package com.green.beadalyo.lhn.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponResponseDto {

    @Schema(description = "쿠폰의 고유 ID", example = "1")
    private Long id;

    @Schema(description = "쿠폰 이름", example = "할인 쿠폰")
    private String name;

    @Schema(description = "쿠폰 내용", example = "오백원깎아드려요")
    private String content;

    @Schema(description = "쿠폰 할인 금액 (고정 금액)", example = "500")
    private Integer price;

    @Schema(description = "쿠폰 생성 일시", example = "2024-08-08T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "쿠폰 최소주문금액", example = "1000")
    private Long minOrderAmount;

    @Schema(description = "식당 이름", example = "김치식당")
    private String resName;

    public CouponResponseDto(Long id, String name, String content, Integer price, Long minOrderAmount, LocalDateTime createdAt , String resName) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.price = price;
        this.createdAt = createdAt;
        this.minOrderAmount = minOrderAmount;
        this.resName = resName;
    }
}