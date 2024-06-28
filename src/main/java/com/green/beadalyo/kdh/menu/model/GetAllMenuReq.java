package com.green.beadalyo.kdh.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class GetAllMenuReq {
    @Schema(name = "menu_res_pk")
    public long menuResPk;

    @ConstructorProperties({"menu_res_pk"})
        public GetAllMenuReq(long menuResPk) {
        this.menuResPk = menuResPk;
    }
}
