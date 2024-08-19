package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrderMenu;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    public DoneOrderMiniGetResUser(Long doneOrderPk, Long resPk, String resPic, String resName, Integer orderPrice, Integer doneOrderState, String createdAt, Integer reviewState) {
        this.doneOrderPk = doneOrderPk;
        this.resPk = resPk;
        this.resPic = resPic;
        this.resName = resName;
        this.orderPrice = orderPrice;
        this.doneOrderState = doneOrderState;
        this.createdAt = createdAt;
        this.reviewState = reviewState;
    }

    public DoneOrderMiniGetResUser(DoneOrder data)
    {
        this.doneOrderPk = data.getDoneOrderPk() ;
        Restaurant res = data.getResPk() ;
        this.resPk = res.getSeq() ;
        this.resPic = res.getPic() ;
        this.resName = res.getName() ;
        this.orderPrice = data.getOrderPrice() ;
        this.doneOrderState = data.getDoneOrderState() ;
        this.createdAt = data.getCreatedAt().toString() ;
        this.menuInfoDtos = data.getDoneOrderMenus().stream().map(MenuInfoDto::new).toList() ;
    }
}
