package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menuoption.model.GetMenuOptionRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAllMenuRes {
    private long menuPk;
    private long menuResPk;
    private String menuName;
    private String menuContent;
    private int menuPrice;
    private String menuPic;
    private int menuState;
    private List<GetMenuOptionRes> list ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetAllMenuRes(MenuEntity menuEntity) {
        this.menuPk = menuEntity.getMenuPk();
        this.menuResPk = menuEntity.getMenuCategory().getRestaurant().getSeq();
        this.menuName = menuEntity.getMenuName();
        this.menuContent = menuEntity.getMenuContent();
        this.menuPrice = menuEntity.getMenuPrice();
        this.menuPic = menuEntity.getMenuPic();
        this.menuState = menuEntity.getMenuState();
        this.createdAt = menuEntity.getCreatedAt();
        this.updatedAt = menuEntity.getUpdatedAt();
    }
}
