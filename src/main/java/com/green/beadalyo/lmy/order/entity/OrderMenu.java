package com.green.beadalyo.lmy.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderMenuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pk", nullable = false)
    private Order orderPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_pk", nullable = false)
    private Menu2 menuPk;

    @Column(length = 20, nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer menuPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
