package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.lmy.order.model.OrderMenuEntity;
import com.green.beadalyo.lmy.order.model.OrderMenuReq;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "`order`")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_pk")
    private Long orderPk;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user_pk", nullable = false)
    private User orderUserPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_res_pk", nullable = false)
    private Restaurant orderResPk;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "orderPk", orphanRemoval = true)
    private List<OrderMenu> menus ;

    @Column(name = "order_price", nullable = false)
    private Integer orderPrice;

    @Column(name = "order_request", length = 500)
    private String orderRequest;

    @Column(name = "order_phone", length = 50)
    private String orderPhone;

    @Column(name = "order_address", length = 50)
    private String orderAddress;

    @Column(name = "order_state")
    @ColumnDefault("1")
    private Integer orderState;

    @Column(name = "order_method")
    @ColumnDefault("0")
    private Integer orderMethod;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_at", nullable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Order(OrderPostReq data, User user, Restaurant res)
    {
        this.orderUserPk = user ;
        this.orderResPk = res ;
        this.menus = null ;
        this.orderPrice = 0 ;
        this.orderRequest = data.getOrderRequest() ;
        this.orderPhone = data.getOrderPhone() ;
        this.orderAddress = data.getOrderAddress() ;
        this.paymentMethod = data.getPaymentMethod() ;
        this.orderState = 1 ;
        this.orderMethod = 0 ;
    }



}



