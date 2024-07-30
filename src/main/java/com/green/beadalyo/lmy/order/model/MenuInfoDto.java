package com.green.beadalyo.lmy.order.model;

import lombok.Data;

@Data
public class MenuInfoDto {
    private String MenuName;
    private Integer MenuPrice;

    public MenuInfoDto(String menuName, Integer menuPrice) {
        this.MenuName = menuName;
        this.MenuPrice = menuPrice;
    }
}
