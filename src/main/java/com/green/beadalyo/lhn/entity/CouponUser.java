package com.green.beadalyo.lhn.entity;

import com.green.beadalyo.jhw.user.entity.User;
import lombok.Data;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "res_coupon_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"res_coupon_pk", "user_pk"}
                )
        }
)
@Data
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_coupon_user_pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_coupon_pk", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false)
    private User user;

    @ColumnDefault("1")
    @Column(name = "res_coupon_state")
    private int state;

    @Column(name = "created_at")
    @CreationTimestamp()
    private LocalDateTime createdAt;
}