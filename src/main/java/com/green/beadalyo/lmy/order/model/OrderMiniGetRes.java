package com.green.beadalyo.lmy.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderMiniGetRes {
    private Long orderPk;
    private Long resPk;
    private String resPic;
    private String resName;
    private Integer orderPrice;
    private Integer orderState;
    private LocalDateTime createdAt;
    private List<String> menuName;

    public OrderMiniGetRes(Long orderPk, Long resPk, String resPic, String resName, Integer orderPrice, Integer orderState, LocalDateTime createdAt) {
        this.orderPk = orderPk;
        this.resPk = resPk;
        this.resPic = resPic;
        this.resName = resName;
        this.orderPrice = orderPrice;
        this.orderState = orderState;
        this.createdAt = createdAt;
    }
}
