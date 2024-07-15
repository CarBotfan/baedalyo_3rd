package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class DoneOrderMiniGetRes {
    private Long doneOrderPk;
    private Long resPk;
    private Integer orderPrice;
    private Integer doneOrderState;
    private String createdAt;
    private List<MenuInfoDto> menuInfoDtos;
}
