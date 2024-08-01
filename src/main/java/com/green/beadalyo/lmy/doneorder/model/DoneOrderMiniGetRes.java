package com.green.beadalyo.lmy.doneorder.model;

import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoneOrderMiniGetRes {
    private Long doneOrderPk;
    private Long resPk;
    private String resPic;
    private String resName;
    private Integer orderPrice;
    private Integer doneOrderState;
    private LocalDateTime createdAt;
    private List<MenuInfoDto> menuInfoDtos;

    public DoneOrderMiniGetRes(Long doneOrderPk, Long resPk, String resPic, String resName, Integer orderPrice, Integer doneOrderState, LocalDateTime createdAt) {
        this.doneOrderPk = doneOrderPk;
        this.resPk = resPk;
        this.resPic = resPic;
        this.resName = resName;
        this.orderPrice = orderPrice;
        this.doneOrderState = doneOrderState;
        this.createdAt = createdAt;
    }
}
