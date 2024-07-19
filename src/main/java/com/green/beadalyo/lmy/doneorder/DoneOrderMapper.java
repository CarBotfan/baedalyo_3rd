package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.lmy.doneorder.model.*;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DoneOrderMapper {
    List<MenuInfoDto> selectDoneMenuInfo(Long doneOrderPk);
    Integer selectTotalElementsByUserPk(Long userPk);
    Integer selectReviewState(Long doneOrderPk);
    Integer selectCancelTotalElementsByResPk(Long resPk);
    Integer selectDoneTotalElementsByResPk(Long resPk);
    List<DoneOrderMiniGetResUser> selectDoneOrderByUserPk(DoneOrderByUserPkDto dto);
//    List<DoneOrderMiniGetRes> selectCancelOrderByUserPk(Long userPk);
    List<DoneOrderMiniGetRes> selectDoneOrderByResPk(DoneOrderByResPkDto dto);
    List<DoneOrderMiniGetRes> selectCancelOrderByResPk(DoneOrderByResPkDto dto);
    DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk);

}
