package com.green.beadalyo.lmy.order.model;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.model.MenuCatGetRes;
import com.green.beadalyo.kdh.menu.model.OrderMenuRes;
import com.green.beadalyo.lmy.order.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<MenuCatGetRes> menuInfoList;

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

    public OrderGetRes(Order data) {
        this.orderPk = data.getOrderPk();
        this.userPk = data.getOrderUser().getUserPk();
        Restaurant res = data.getOrderRes() ;
        this.resPk = res.getSeq();
        this.resName = res.getName();
        this.orderAddress = data.getOrderAddress();
        this.orderPhone = data.getOrderPhone();
        this.orderPrice = data.getOrderPrice();
        this.orderRequest = data.getOrderRequest();
        this.orderState = data.getOrderState();
        this.paymentMethod = data.getPaymentMethod();
        this.createdAt = data.getCreatedAt();
        this.menuInfoList = new ArrayList<>();
        data.getMenus().forEach(orderMenu -> {
            OrderMenuRes menu = new OrderMenuRes(orderMenu) ;
        });
    }
}
