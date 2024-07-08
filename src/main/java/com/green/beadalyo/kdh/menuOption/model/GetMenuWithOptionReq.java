package com.green.beadalyo.kdh.menuOption.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class GetMenuWithOptionReq {
    @Schema(name = "menu_pk")
    public long menuPk;

    @ConstructorProperties({"menu_pk"})
    public GetMenuWithOptionReq(long menuPk) {
        this.menuPk = menuPk;
    }
}
