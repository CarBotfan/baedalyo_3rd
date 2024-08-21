package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DoneOrderGetRes {
    private Long doneOrderPk;
    private Long userPk;
    private Long resPk;
    private String resName;
    private String orderAddress;
    private String orderPhone;
    private Integer orderPrice;
    private String orderRequest;
    private Integer doneOrderState;
    private Integer paymentMethod;
    private LocalDateTime createdAt;
    private CouponRes coupon;
    private List<MenuInfoDto> menuInfoList;

    public void setCoupon(CouponUser data)
    {
        if (data == null) return ;
        this.coupon = new CouponRes(data) ;
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
