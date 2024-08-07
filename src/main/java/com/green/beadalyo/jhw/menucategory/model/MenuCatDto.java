package com.green.beadalyo.jhw.menucategory.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuCatDto {
    private String menuCatName;
    private Long position;

    public MenuCatDto(MenuCategory menuCat) {
        this.menuCatName = menuCat.getMenuCatName();
        this.position = menuCat.getPosition();
    }
}
