package com.green.beadalyo.jhw.menucategory.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
