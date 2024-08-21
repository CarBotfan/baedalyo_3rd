package com.green.beadalyo.lmy.order.model;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.kdh.menu.model.OrderMenuRes;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderMiniGetRes {
    private Long orderPk;
    private Long resPk;
    private String resPic;
    private String resName;
    private Integer orderPrice;
    private Integer orderState;
    private String orderRequest ;
    private String orderAddress ;
    private String orderPhone ;
    private CouponRes orderCoupon;
    private LocalDateTime createdAt;
    private Integer paymentMethod ;
    private List<OrderMenuRes> menus;

    public OrderMiniGetRes(Long orderPk, Long resPk, String resPic, String resName, Integer orderPrice, Integer orderState, LocalDateTime createdAt) {
        this.orderPk = orderPk;
        this.resPk = resPk;
        this.resPic = resPic;
        this.resName = resName;
        this.orderPrice = orderPrice;
        this.orderState = orderState;
        this.createdAt = createdAt;
    }

    public OrderMiniGetRes(Order data)
    {
        this.orderPk = data.getOrderPk() ;
        Restaurant res = data.getOrderRes();
        this.resPk = res.getSeq() ;
        this.resPic = res.getPic();
        this.resName = res.getName();
        this.orderPrice = data.getOrderPrice();
        this.orderState = data.getOrderState();
        this.createdAt = data.getCreatedAt();
        this.orderAddress = data.getOrderAddress();
        this.orderPhone = data.getOrderPhone();
        this.paymentMethod = data.getPaymentMethod();
        this.orderRequest = data.getOrderRequest();
        if (data.getCoupon() != null)
            this.orderCoupon = new CouponRes(data.getCoupon()) ;
        this.menus = new ArrayList<>();
        data.getMenus().forEach(orderMenu ->
            {
                OrderMenuRes orderMenus = new OrderMenuRes(orderMenu);
                this.menus.add(orderMenus) ;

            }
        );


    }

}

@Data
class CouponRes{
    private String couponName ;
    private Long couponPk ;
    private Integer couponPrice ;
    private String couponRestaurant ;

    public CouponRes(CouponUser data)
    {
        this.couponName = data.getCoupon().getName() ;
        this.couponPk = data.getId() ;
        this.couponPrice = data.getCoupon().getPrice() ;
        this.couponRestaurant = data.getCoupon().getRestaurant().getName() ;
    }
}
