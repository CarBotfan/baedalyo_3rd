package com.green.beadalyo.kdh.menu.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PutMenuRes {
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
