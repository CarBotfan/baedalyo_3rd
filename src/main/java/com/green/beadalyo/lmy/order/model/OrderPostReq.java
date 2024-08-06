package com.green.beadalyo.lmy.order.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.tomcat.util.security.Escape;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderPostReq {

    @Schema(example = "1", description = "레스토랑 번호")
    private Long orderResPk;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "요청사항" , description = "주문요구사항")
    private String orderRequest;
    @Schema(example = "결제수단 키", description = "1 : 현금결제(후불) / 2 : 카드결제(후불) / 3 : 통합모듈 결제(선불) / 4 : 계좌이체 결제(선불) / 5 : 가상계좌 결제(선불) / 6 : 휴대폰 결제(선불)")
    private Integer paymentMethod;
    @Schema(example = "전화번호", description = "주문 전화번호")
    private String orderPhone;
    @Schema(example = "배달주소", description = "배달 주소")
    private String orderAddress;
    @Schema(description = "메뉴 정보")
    private List<OrderMenuReq> menu;
    @Schema(description = "유저 마일리지 사용")
    private Integer useMileage ;
    @Schema(description = "사용할 쿠폰 고유번호")
    private Long coupon ;
}

//    @JsonIgnore
//    private Long orderPk;        // 자동 생성된 orderPk를 받기 위한 필드
//    @JsonIgnore
//    private Long orderUserPk;
//
//    @JsonIgnore
//    private Integer orderPrice;  // 총 가격 필드 추가
//
//    @JsonIgnore
//    private Long orderMenuPk;