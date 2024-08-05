package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "done_order_menu")
public class DoneOrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_order_menu_pk")
    private Long doneOrderMenuPk;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "done_order_pk",nullable = false)
    private DoneOrder doneOrderPk;

    @OneToMany(fetch = FetchType.EAGER,orphanRemoval = true,cascade = CascadeType.ALL, mappedBy = "doneOrderMenu")
    private List<DoneOrderMenuOption> doneMenuOption ;

    @ManyToOne
    @JoinColumn(name = "menu_pk", nullable = false)
    private MenuEntity menuPk;

    @Column(name = "menu_name", length = 20, nullable = false )
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
