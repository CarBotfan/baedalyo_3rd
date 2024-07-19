package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.lmy.doneorder.model.DoneOrderByResPkDto;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderByUserPkDto;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DoneOrderMapper {
    List<MenuInfoDto> selectDoneMenuInfo(Long doneOrderPk);
    Integer selectTotalElementsByUserPk(Long userPk);
    Integer selectTotalElementsByResPk(Long resPk);
    List<DoneOrderMiniGetRes> selectDoneOrderByUserPk(DoneOrderByUserPkDto dto);
//    List<DoneOrderMiniGetRes> selectCancelOrderByUserPk(Long userPk);
    List<DoneOrderMiniGetRes> selectDoneOrderByResPk(DoneOrderByResPkDto dto);
    List<DoneOrderMiniGetRes> selectCancelOrderByResPk(DoneOrderByResPkDto dto);
    DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk);

}
