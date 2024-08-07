package com.green.beadalyo.kdh.menuoption.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetMenuOptionRes {
    private long optionPk;
    private long optionMenuPk;
    private String optionName;
    private int optionPrice;
    private int optionState;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetMenuOptionRes(long optionPk, long optionMenuPk, String optionName, int optionPrice, int optionState, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.optionPk = optionPk;
        this.optionMenuPk = optionMenuPk;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.optionState = optionState;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
