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
    private Long menuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_res_pk", nullable = false)
    private Restaurant menuResPk;

    @Column(length = 20, nullable = false)
    private String menuName;

    private String menuContent;

    @Column(nullable = false)
    private Integer menuPrice;

    private String menuPic;

    @ColumnDefault("1")
    private Integer menuState;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
