package com.green.beadalyo.jhw.menucategory.model;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MenuCatDto {
    private Long menuCatPk;
    private String menuCatName;
    private Long position;

    public MenuCatDto(MenuCategory menuCat) {
        this.menuCatPk = menuCat.getMenuCategoryPk();
        this.menuCatName = menuCat.getMenuCatName();
        this.position = menuCat.getPosition();
    }
}
