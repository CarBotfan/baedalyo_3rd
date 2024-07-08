package com.green.beadalyo.kdh.menuOption.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMenuOptionReq {
    @JsonIgnore
    private long optionPk;
    private long optionMenuPk;
    private String option1Name;
    private String option2Name;
    private int optionPrice;
    private int optionState;
}
