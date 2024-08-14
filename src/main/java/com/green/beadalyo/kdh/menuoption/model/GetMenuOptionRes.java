package com.green.beadalyo.kdh.menuoption.model;

import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMenuOptionRes {
    private long optionPk;
    private long menuPk;
    private String optionName;
    private int optionPrice;
    private int optionState;


    public GetMenuOptionRes(long optionPk, long menuPk, String optionName, int optionPrice, int optionState) {
        this.optionPk = optionPk;
        this.menuPk = menuPk;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.optionState = optionState;

    }

    public GetMenuOptionRes(MenuOption data)
    {

    }
}
