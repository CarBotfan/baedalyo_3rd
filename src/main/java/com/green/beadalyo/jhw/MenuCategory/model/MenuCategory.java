package com.green.beadalyo.jhw.MenuCategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "menu_category")
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_category_pk")
    private Long menuCategoryPk;

    @ManyToOne
    @JoinColumn(name = "restaurant_pk", nullable = false)
    private Restaurant restaurant;

    @Column(name = "menu_category_name", nullable = false)
    private String menuCategoryName;

    @Column(name = "menu_category_seq", nullable = false)
    private Long menuCategorySeq;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
