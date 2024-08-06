package com.green.beadalyo.kdh.menuOption.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMenuOptionReq {
    @Schema()
    private long optionMenuPk;
    @Schema()
    private String optionName;
    @Schema()
    private int optionPrice;
}
