package com.green.beadalyo.kdh.menuoption.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostMenuOptionRes {
    private long optionPk;
    private long optionMenuPk;
    private String optionName;
    private int optionPrice;
    private int optionState;
}
