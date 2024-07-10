package com.green.beadalyo.lmy.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.util.List;

@Data
public class OrderPostReq {
    @JsonIgnore
    private Long orderPk;        // 자동 생성된 orderPk를 받기 위한 필드
    @JsonIgnore
    private Long orderUserPk;
    private Long orderResPk;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderRequest;
    private String paymentMethod;
    @JsonIgnore
    private Integer orderPrice;  // 총 가격 필드 추가

    @JsonIgnore
    private Long orderMenuPk;
    private List<Long> menuPk;
}
