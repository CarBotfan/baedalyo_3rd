package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_pk")
    private Long orderMenuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pk", nullable = false)
    private Order orderPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_pk", nullable = false)
    private MenuEntity menuPk;

    @Column(name = "menu_name", length = 20, nullable = false)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
