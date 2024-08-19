package com.green.beadalyo.jhw.menucategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuCatPatchDto {
    private Long menuCatPk;
    private String menuCatName;
    private Restaurant restaurant;

    public MenuCatPatchDto(MenuCatPatchReq p) {
        this.menuCatPk = p.getMenuCatPk();
        this.menuCatName = p.getMenuCatName();
    }
}
