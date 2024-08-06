package com.green.beadalyo.kdh.menuOption.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMenuOptionRes {
    private long optionPk;
    private long optionMenuPk;
    private String optionName;
    private int optionPrice;
    private int optionState;
    private String createdAt;
    private String updatedAt;
}
