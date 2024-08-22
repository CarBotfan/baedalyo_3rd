package com.green.beadalyo.jhw.menucategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.CreationTimestamp;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "menu_category")
@NoArgsConstructor
@ToString
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_cat_pk")
    private Long menuCategoryPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk")
    private Restaurant restaurant;

    @Column(name = "menu_cat_name", length = 2000, nullable = false)
    private String menuCatName;

    @Column(name = "position", nullable = false)
    private Long position;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public MenuCategory(MenuCatInsertDto dto) {
        this.restaurant = dto.getRestaurant();
        this.menuCatName = dto.getMenuCatName();
        this.position = dto.getPosition();
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuCategory")
    private List<MenuEntity> menuList;

    @PreRemove
    public void onPreRemove() {
        this.restaurant = null;
        if(menuList != null) {
            for(MenuEntity menu : menuList) {
                menu.setMenuCategory(null);
                menu.setMenuState(3);
            }
        }
    }
}





