package com.green.beadalyo.lmy.order.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderGetRes {
    private Long orderPk;
    private Long userPk;
    private Long resPk;
    private String orderAddress;
    private Integer orderPrice;
    private String orderRequest;
    private Integer orderState;
    private String paymentMethod;
    private String createdAt;
    private List<MenuInfoDto> menuInfoList;
}
