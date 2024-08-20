package com.green.beadalyo.kdh.menu.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_pk")
    private Long menuPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_cat_pk")
    private MenuCategory menuCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "menu", orphanRemoval = true)
    private List<MenuOption> optionList ;

    @Column(name = "menu_name", length = 2000, nullable = false)
    private String menuName;

    @Column(name = "menu_content", length = 2000)
    private String menuContent;

    @Column(nullable = false, name = "menu_price")
    private Integer menuPrice;

    @Column(name = "menu_pic" , length = 2000)
    private String menuPic;

    @ColumnDefault("1")
    @Column(name = "menu_state")
    private Integer menuState;

    @Column(name = "created_at", updatable = false ,nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PreRemove
    public void preRemove() {
        this.menuCategory = null;
    }

}
