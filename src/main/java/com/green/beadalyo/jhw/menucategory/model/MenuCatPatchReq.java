package com.green.beadalyo.jhw.menucategory.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MenuCatPatchReq {
    private Long menuCatPk;
    private String menuCatName;
}
