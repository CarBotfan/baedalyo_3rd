package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Menu2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_pk")
    private Long menuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_res_pk", nullable = false)
    private Restaurant menuResPk;

    @Column(length = 20, nullable = false, name = "menu_name")
    private String menuName;

    @Column(name = "menu_content")
    private String menuContent;

    @Column(nullable = false, name = "menu_price")
    private Integer menuPrice;

    @Column(name = "menu_pic")
    private String menuPic;

    @ColumnDefault("1")
    @Column(name = "menu_state")
    private Integer menuState;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
