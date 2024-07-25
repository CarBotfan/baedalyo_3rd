package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private User userPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk", nullable = false)
    private Restaurant resPk;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(length = 500)
    private String orderRequest;

    private String orderPhone;

    private String orderAddress;

    private String paymentMethod;

    private Integer orderMethod;

    private Integer doneOrderState;

    private String canceller;

    private String createdAt;
}
