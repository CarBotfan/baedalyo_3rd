package com.green.beadalyo.jhw.menucategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuCatPositionPatchDto {
    private Long menuCatPk1;
    private Long position;
    private Restaurant restaurant;

    public MenuCatPositionPatchDto(MenuCatPositionPatchReq p) {
        this.menuCatPk1 = p.getMenuCatPk1();
        this.position = p.getPosition();
    }
}
