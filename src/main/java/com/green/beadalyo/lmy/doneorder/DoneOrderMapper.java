package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DoneOrderMapper {
    List<MenuInfoDto> selectDoneMenuInfo(Long doneOrderPk);
    List<DoneOrderMiniGetRes> selectDoneOrderByUserPk(Long userPk);
    List<DoneOrderMiniGetRes> selectCancelOrderByUserPk(Long userPk);
    List<DoneOrderMiniGetRes> selectDoneOrderByResPk(Long resPk);
    List<DoneOrderMiniGetRes> selectCancelOrderByResPk(Long resPk);
    DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk);
}
