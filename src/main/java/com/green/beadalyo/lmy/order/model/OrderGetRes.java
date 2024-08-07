package com.green.beadalyo.lmy.order.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderGetRes {
    private Long orderPk;
    private Long userPk;
    private Long resPk;
    private String resName;
    private String orderAddress;
    private String orderPhone;
    private Integer orderPrice;
    private String orderRequest;
    private Integer orderState;
    private Integer paymentMethod;
    private LocalDateTime createdAt;
    private List<MenuInfoDto> menuInfoList;

    public OrderGetRes(Long orderPk, Long userPk, Long resPk, String resName, String orderAddress, String orderPhone, Integer orderPrice, String orderRequest, Integer orderState, Integer paymentMethod, LocalDateTime createdAt) {
        this.orderPk = orderPk;
        this.userPk = userPk;
        this.resPk = resPk;
        this.resName = resName;
        this.orderAddress = orderAddress;
        this.orderPhone = orderPhone;
        this.orderPrice = orderPrice;
        this.orderRequest = orderRequest;
        this.orderState = orderState;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }
}
