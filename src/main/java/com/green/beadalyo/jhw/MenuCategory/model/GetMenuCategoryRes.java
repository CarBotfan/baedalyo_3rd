package com.green.beadalyo.jhw.MenuCategory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetMenuCategoryRes {
    private String menuCatName;
    private Long position;

    public GetMenuCategoryRes(MenuCategory menuCat) {
        this.menuCatName = menuCat.getMenuCatName();
        this.position = menuCat.getPosition();
    }
}
