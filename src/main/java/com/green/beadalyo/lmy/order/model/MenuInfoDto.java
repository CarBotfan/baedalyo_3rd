package com.green.beadalyo.lmy.order.model;

import com.green.beadalyo.lmy.doneorder.entity.DoneOrderMenu;
import lombok.Data;

@Data
public class MenuInfoDto {
    private String MenuName;
    private Integer MenuPrice;

    public MenuInfoDto(String menuName, Integer menuPrice) {
        this.MenuName = menuName;
        this.MenuPrice = menuPrice;
    }

    public MenuInfoDto(DoneOrderMenu data)
    {
        this.MenuName = data.getMenuName();
        this.MenuPrice = data.getMenuPrice();
    }
}
