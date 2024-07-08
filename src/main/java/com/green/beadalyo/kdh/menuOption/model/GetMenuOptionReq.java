package com.green.beadalyo.kdh.menuOption.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class GetMenuOptionReq {
    @Schema(name = "option_pk")
    private long optionPk;

    @ConstructorProperties({"option_pk"})
    public GetMenuOptionReq(long optionPk){
        this.optionPk = optionPk;
    }
}
