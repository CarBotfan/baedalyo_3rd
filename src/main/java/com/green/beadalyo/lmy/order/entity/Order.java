package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
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


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user_pk", nullable = false)
    private User orderUser;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_res_pk", nullable = false)
    private Restaurant orderRes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderMenu> menus;

    @Column(name = "order_price", nullable = false)
    @Comment("할인전 최종 금액")
    private Integer orderPrice;

    @Column(name = "order_total_price", nullable = false)
    @Comment("할인후 최종 금액")
    private Integer totalPrice;

    @Column(name = "order_request", length = 500)
    private String orderRequest;

    @Column(name = "order_phone", length = 50)
    private String orderPhone;

    @Column(name = "order_address", length = 50)
    private String orderAddress;

    @Column(name = "order_state")
    @ColumnDefault("1")
    @Comment("결제 진행상황 1 : 결제 전 / 2 : 결제 후 / 3 : 주문 접수")
    private Integer orderState;

    @Column(name = "order_method")
    @ColumnDefault("0")
    private Integer orderMethod;

    @Column(name = "payment_method")
    @Comment("1 : 현금결제(후불) / 2 : 카드결제(후불) / 3 : 통합모듈 결제(선불) / 4 : 계좌이체 결제(선불) / 5 : 가상계좌 결제(선불) / 6 : 휴대폰 결제(선불)")
    private Integer paymentMethod;

    @Column(name = "use_mileage", nullable = false)
    @ColumnDefault("0")
    private Integer useMileage;

    @Column(name = "created_at", nullable = true)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Order(OrderPostReq data, User user, Restaurant res)
    {
        this.orderUser = user ;
        this.orderRes = res ;
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



