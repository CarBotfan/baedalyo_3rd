package com.green.beadalyo.kdh.menu.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuPatchCategoryDto {
    private Long menuPk;
    private Long menuCatPk;
    private Restaurant restaurant;
    public MenuPatchCategoryDto(MenuPatchCategoryReq p) {
        this.menuPk = p.getMenuPk();
        this.menuCatPk = p.getMenuCatPk();
    }
}
