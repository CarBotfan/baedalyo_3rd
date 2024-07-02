package com.green.beadalyo.lmy.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderGetRes {
    private Long orderPk;
    private Long resPk;
    private Integer orderPrice;
    private Integer orderState;
    private List<String> menuName;
}
