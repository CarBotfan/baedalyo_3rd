package com.green.beadalyo.lmy.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderPostReq {
    @JsonIgnore
    private Long orderPk;        // 자동 생성된 orderPk를 받기 위한 필드
    @JsonIgnore
    private Long orderUserPk;
    @Schema(example = "1")
    private Long orderResPk;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "요청사항")
    private String orderRequest;
    @Schema(example = "결제수단 키")
    private String paymentMethod;
    @Schema(example = "전화번호")
    private String orderPhone;
    @Schema(example = "배달주소")
    private String orderAddress;

    @JsonIgnore
    private Integer orderPrice;  // 총 가격 필드 추가

    @JsonIgnore
    private Long orderMenuPk;
    private List<Long> menuPk;
}
