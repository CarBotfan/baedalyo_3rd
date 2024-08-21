package com.green.beadalyo.lhn.coupon.entity;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.order.entity.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "res_coupon_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"res_coupon_pk", "user_pk"}
                )
        }
)
@Getter
@Setter
@ToString
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_coupon_user_pk")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_coupon_pk", nullable = false)
    private Coupon coupon;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false)
    private User user;

    @ColumnDefault("1")
    @Column(name = "res_coupon_state")
    private int state;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "coupon")
    private DoneOrder doneOrder ;

    @Column(name = "created_at")
    @CreationTimestamp()
    private LocalDateTime createdAt;
}