package com.green.beadalyo.lmy.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderPostReq {
    @JsonIgnore
    private Long orderPk;
    private Long orderUserPk;
    private Long orderResPk;
    private Integer orderPrice;
    private String orderRequest;
    private String paymentMethod;

    @JsonIgnore
    private Long orderMenuPk;
    private Long menuPk;

    private Long optionPk;
}
