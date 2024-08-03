package com.green.beadalyo.jhw.MenuCategory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuCategoryGetRes {
    private Long MenuCatPk;
    private String menuCatName;
    private Long position;

    public MenuCategoryGetRes(MenuCategory menuCat) {
        this.MenuCatPk = menuCat.getMenuCategoryPk();
        this.menuCatName = menuCat.getMenuCatName();
        this.position = menuCat.getPosition();
    }
}
