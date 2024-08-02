package com.green.beadalyo.kdh.menu.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_pk")
    private Long menuPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_res_pk", nullable = false)
    private Restaurant menuResPk;

    @Column(name = "menu_name", length = 20, nullable = false)
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