package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetOneMenuReq {
    @Schema(name = "menu_pk")
    public long menuPk;

    @ConstructorProperties({"menu_pk"})
        public GetOneMenuReq(long menuPk) {
        this.menuPk = menuPk;
    }
}
