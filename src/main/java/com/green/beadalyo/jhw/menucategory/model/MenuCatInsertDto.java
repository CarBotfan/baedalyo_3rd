package com.green.beadalyo.jhw.menucategory.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuCatInsertDto {
    private Restaurant restaurant;
    private String menuCatName;
    private Long position;
}
