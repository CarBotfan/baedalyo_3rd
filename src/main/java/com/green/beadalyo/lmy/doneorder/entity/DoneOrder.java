package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "done_order")
public class DoneOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_order_pk")
    private Long doneOrderPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false, referencedColumnName = "user_pk")
    private User userPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk", nullable = false, referencedColumnName = "res_pk")
    private Restaurant resPk;

    @Column(name = "order_price", nullable = false)
    private Integer orderPrice;

    @Column(name = "order_request", length = 500)
    private String orderRequest;

    @Column(name = "order_phone", length = 50)
    private String orderPhone;

    @Column(name = "order_address", length = 50)
    private String orderAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @ColumnDefault("0")
    @Column(name = "order_method")
    private Integer orderMethod;

    @Column(name = "done_order_state")
    private Integer doneOrderState;

    @Column(name = "canceller")
    private String canceller;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
