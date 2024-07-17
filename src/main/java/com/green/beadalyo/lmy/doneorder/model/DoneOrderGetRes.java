package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class DoneOrderGetRes {
    private Long doneOrderPk;
    private Long userPk;
    private Long resPk;
    private String orderAddress;
    private String orderPhone;
    private Integer orderPrice;
    private String orderRequest;
    private Integer doneOrderState;
    private String paymentMethod;
    private String createdAt;
    private List<MenuInfoDto> menuInfoList;
}
