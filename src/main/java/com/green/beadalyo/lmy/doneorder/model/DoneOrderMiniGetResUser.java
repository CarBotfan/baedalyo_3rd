package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class DoneOrderMiniGetResUser {
    private Long doneOrderPk;
    private Long resPk;
    private String resPic;
    private String resName;
    private Integer orderPrice;
    private Integer doneOrderState;
    private Integer reviewState;
    private String createdAt;
    private List<MenuInfoDto> menuInfoDtos;
}
