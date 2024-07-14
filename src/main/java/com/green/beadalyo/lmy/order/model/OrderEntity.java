package com.green.beadalyo.lmy.order.model;

import lombok.Data;

@Data
public class OrderEntity {
    private Long doneOrderPk;
    private Long orderUserPk;
    private Long orderResPk;
    private Integer orderPrice;
    private String orderRequest;
    private Integer orderState;
    private String paymentMethod;
    private String canceller;
}
