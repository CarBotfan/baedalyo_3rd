package com.green.beadalyo.kdh.menuOption.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutMenuOptionReq {
    private long optionPk;
    private String option1Name;
    private String option2Name;
    private int optionPrice;
    private int optionState;
}
