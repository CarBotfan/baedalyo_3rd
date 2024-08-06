package com.green.beadalyo.kdh.menuOption.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutMenuOptionReq {
    private long optionPk;
    private String optionName;
    private int optionPrice;
}
