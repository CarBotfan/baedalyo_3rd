package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order")
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

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}


