package com.green.beadalyo.lhn.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserResponseDto {
    @Schema(description = "리뷰 댓글의 고유 pk", example = "1")
    private Long id;

    @Schema(description = "쿠폰의 고유 ID", example = "10")
    private Long couponId;

    @Schema(description = "쿠폰 내용", example = "오백원깎아드려요")
    private String content;

    @Schema(description = "쿠폰 할인 금액 (고정 금액)", example = "500")
    private Integer price;

    @Schema(description = "최소 주문 금액", example = "1000")
    private Long minOrderAmount;

    @Schema(description = "쿠폰 이름", example = "할인 쿠폰")
    private String name;
}