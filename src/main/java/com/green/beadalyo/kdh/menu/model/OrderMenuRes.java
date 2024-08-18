package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderMenuRes
{
    private Long orderMenuPk ;
    private String orderMenuName ;
    private Integer orderMenuPrice ;
    private Integer orderMenuCount ;
    private List<OrderMenuOptionRes> menuOptions ;

//    public OrderMenuRes(OrderCategory data)
//    {
//        this.orderMenuPk = data.getOrderMenuPk();
//        this.orderMenuName = data.getMenuName() ;
//        this.orderMenuCount = data.getMenuCount() ;
//        this.orderMenuPrice = data.getMenuPrice() ;
//        this.menuOptions = new ArrayList<>();
//        for (OrderMenuOption i : data.getOrderMenuOption()) {
//            OrderMenuOptionRes option = new OrderMenuOptionRes(i);
//            option.setOptionMenuPk(data.getOrderMenuPk());
//            menuOptions.add(option);
//        }
//
//    }

    public OrderMenuRes(OrderMenu data)
    {
        this.orderMenuPk = data.getOrderMenuPk() ;
        this.orderMenuName = data.getMenuName() ;
        this.orderMenuPrice = data.getMenuPrice() ;
        this.orderMenuCount = data.getMenuCount() ;
        this.menuOptions = new ArrayList<>() ;
        data.getOrderMenuOption().forEach(orderMenuOption ->{
            OrderMenuOptionRes orderMenuOptionRes = new OrderMenuOptionRes(orderMenuOption) ;
            orderMenuOptionRes.setOptionMenuPk(this.orderMenuPk);
            menuOptions.add(orderMenuOptionRes) ;
        });

    }

}
