package com.green.beadalyo.lhn.coupon.entity;

import com.green.beadalyo.gyb.model.Restaurant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "res_coupon")
@Data
@ToString
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_coupon_pk")
    private Long id;

    @ToString.Exclude
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk", nullable = false)
    private Restaurant restaurant;

    @Column(name = "res_name" , nullable = false)
    private String resName;

    @Column(name = "res_coupon_name")
    private String name;

    @Column(name = "res_coupon_content")
    private String content;

    @Column(name = "res_coupon_price")
    private Integer price;

    @Column(name = "created_at")
    @CreationTimestamp()
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponUser> couponUser;

    @Column(name = "min_order_amount")
    private Long minOrderAmount; // 최소 주문 금액 필드 추가

    @ColumnDefault("1")
    @Column(name = "coupon_state")
    private int state;

}