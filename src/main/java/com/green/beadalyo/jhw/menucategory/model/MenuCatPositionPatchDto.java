package com.green.beadalyo.jhw.menucategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuCatPositionPatchDto {
    private Long menuCatPk;
    private Long position;
    private Restaurant restaurant;

    public MenuCatPositionPatchDto(MenuCatPositionPatchReq p) {
        this.menuCatPk = p.getMenuCatPk();
        this.position = p.getPosition();
    }
}
