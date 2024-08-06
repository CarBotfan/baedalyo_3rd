package com.green.beadalyo.jhw.menucategory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuCatGetRes {
    private Long MenuCatPk;
    private String menuCatName;
    private Long position;

    public MenuCatGetRes(MenuCategory menuCat) {
        this.MenuCatPk = menuCat.getMenuCategoryPk();
        this.menuCatName = menuCat.getMenuCatName();
        this.position = menuCat.getPosition();
    }
}
