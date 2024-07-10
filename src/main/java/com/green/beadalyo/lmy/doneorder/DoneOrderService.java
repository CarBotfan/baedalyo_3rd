package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoneOrderService {
    private final DoneOrderMapper doneOrderMapper;
    private final AuthenticationFacade authenticationFacade;

    public List<DoneOrderMiniGetRes> getDoneOrderByUserPk() {
        long userPk = authenticationFacade.getLoginUserPk();

        List<DoneOrderMiniGetRes> result = null;

        result = doneOrderMapper.selectDoneOrderByUserPk(userPk);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        return result;
    }

    public List<DoneOrderMiniGetRes> getCancelOrderByUserPk() {
        long userPk = authenticationFacade.getLoginUserPk();

        List<DoneOrderMiniGetRes> result = null;

        result = doneOrderMapper.selectCancelOrderByUserPk(userPk);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }


        return result;
    }

    public List<DoneOrderMiniGetRes> getDoneOrderByResPk(Long resPk) {
        List<DoneOrderMiniGetRes> result = null;

        result = doneOrderMapper.selectDoneOrderByResPk(resPk);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        return result;
    }

    public List<DoneOrderMiniGetRes> getCancelOrderByResPk(Long resPk) {
        List<DoneOrderMiniGetRes> result = null;

        result = doneOrderMapper.selectCancelOrderByResPk(resPk);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        return result;
    }

    public DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk) {
        DoneOrderGetRes result = null;

        result = doneOrderMapper.getDoneOrderInfo(doneOrderPk);
        result.setMenuInfoList(doneOrderMapper.selectDoneMenuInfo(doneOrderPk));

        return result;
    }


}
