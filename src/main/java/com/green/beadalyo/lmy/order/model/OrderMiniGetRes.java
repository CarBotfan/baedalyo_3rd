package com.green.beadalyo.lmy.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderMiniGetRes {
    private Long orderPk;
    private Long resPk;
    private Integer orderPrice;
    private Integer orderState;
    private String createdAt;
    private List<String> menuName;
}
