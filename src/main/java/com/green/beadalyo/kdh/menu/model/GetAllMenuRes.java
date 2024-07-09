package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAllMenuRes {
    private long menuPk;
    private long menuResPk;
    private String menuName;
    private String menuContent;
    private int menuPrice;
    private String menuPic;
    private int menuState;
    private String createdAt;
    private String updatedAt;
}
