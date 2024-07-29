package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderPk;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user_pk", nullable = false)
    private UserEntity orderUserPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_res_pk", nullable = false)
    private Restaurant orderResPk;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(length = 500)
    private String orderRequest;

    @Column(length = 50)
    private String orderPhone;

    @Column(length = 50)
    private String orderAddress;

    @ColumnDefault("1")
    private Integer orderState;

    @ColumnDefault("0")
    private Integer orderMethod;

    private String paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
