package com.green.beadalyo.lmy.order.model;

import lombok.Data;

@Data
public class OrderMenuEntity {
    private Long doneOrderPk;
    private Long menuPk;
    private String menuName;
    private Integer menuPrice;
}
