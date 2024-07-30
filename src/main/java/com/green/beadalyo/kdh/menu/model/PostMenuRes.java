package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostMenuRes {
    private long menuPk;
    private long menuResPk;
    private String menuName;
    private String menuContent;
    private int menuPrice;
    private String menuPic;
    private int menuState;


}
