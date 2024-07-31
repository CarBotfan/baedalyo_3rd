package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.lmy.order.entity.Menu2;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "done_order_menu")
public class DoneOrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_order_menu_pk")
    private Long doneOrderMenuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "done_order_pk",nullable = false)
    private DoneOrder doneOrderPk;

    @ManyToOne
    @JoinColumn(name = "menu_pk", nullable = false)
    private Menu2 menuPk;

    @Column(name = "menu_name", length = 20, nullable = false )
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
