package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.doneorder.model.*;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoneOrderService {
    private final DoneOrderMapper doneOrderMapper;
    private final AuthenticationFacade authenticationFacade;

    public Map<String, Object> getDoneOrderByUserPk(Paging p) {
        long userPk = authenticationFacade.getLoginUserPk();

        List<DoneOrderMiniGetRes> result = null;

        Integer totalElements = doneOrderMapper.selectTotalElementsByUserPk(userPk);
        Integer totalPage = (totalElements / p.getSize()) + 1;

        DoneOrderByUserPkDto dto = DoneOrderByUserPkDto.builder()
                .page(p.getPage())
                .size(p.getSize())
                .startIndex(p.getStartIndex())
                .userPk(userPk)
                .build();

        result = doneOrderMapper.selectDoneOrderByUserPk(dto);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

//    public List<DoneOrderMiniGetRes> getCancelOrderByUserPk() {
//        long userPk = authenticationFacade.getLoginUserPk();
//
//        List<DoneOrderMiniGetRes> result = null;
//
//        result = doneOrderMapper.selectCancelOrderByUserPk(userPk);
//        for (DoneOrderMiniGetRes item : result) {
//            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
//            item.setMenuInfoDtos(result2);
//        }
//
//
//        return result;
//    }

    public Map<String, Object> getDoneOrderByResPk(Long resPk, Paging p) {
        List<DoneOrderMiniGetRes> result = null;

        Integer totalElements = doneOrderMapper.selectDoneTotalElementsByResPk(resPk);
        Integer totalPage = totalElements % p.getSize() == 0 ?
                totalElements / p.getSize() : (totalElements / p.getSize()) + 1;

        DoneOrderByResPkDto dto = DoneOrderByResPkDto.builder()
                .page(p.getPage())
                .size(p.getSize())
                .startIndex(p.getStartIndex())
                .resPk(resPk)
                .build();

        result = doneOrderMapper.selectDoneOrderByResPk(dto);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

    public Map<String, Object> getCancelOrderByResPk(Long resPk, Paging p) {
        List<DoneOrderMiniGetRes> result = null;

        Integer totalElements = doneOrderMapper.selectCancelTotalElementsByResPk(resPk);
        Integer totalPage = totalElements % p.getSize() == 0 ?
                totalElements / p.getSize() : (totalElements / p.getSize()) + 1;

        DoneOrderByResPkDto dto = DoneOrderByResPkDto.builder()
                .page(p.getPage())
                .size(p.getSize())
                .startIndex(p.getStartIndex())
                .resPk(resPk)
                .build();

        result = doneOrderMapper.selectCancelOrderByResPk(dto);
        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMapper.selectDoneMenuInfo(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

    public DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk) {
        DoneOrderGetRes result = null;

        result = doneOrderMapper.getDoneOrderInfo(doneOrderPk);
        result.setMenuInfoList(doneOrderMapper.selectDoneMenuInfo(doneOrderPk));

        return result;
    }


}
