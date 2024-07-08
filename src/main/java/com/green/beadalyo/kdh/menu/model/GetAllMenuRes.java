package com.green.beadalyo.kdh.menu.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
