package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoneOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doneOrderPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false)
    private UserEntity userPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk", nullable = false)
    private Restaurant resPk;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(length = 500)
    private String orderRequest;

    @Column(length = 50)
    private String orderPhone;

    @Column(length = 50)
    private String orderAddress;

    private String paymentMethod;

    @ColumnDefault("0")
    private Integer orderMethod;

    private Integer doneOrderState;

    private String canceller;

    @CreationTimestamp
    private String createdAt;
}
