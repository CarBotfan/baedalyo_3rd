package com.green.beadalyo.kdh.menuOption.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class DelMenuOptionReq {
    @Schema(name = "option_pk")
    private long optionPk;

    @ConstructorProperties({"option_pk"})
    public DelMenuOptionReq(long optionPk){
        this.optionPk = optionPk;
    }
}
