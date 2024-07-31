package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.model.*;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderMenuRepository;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoneOrderService {
    private final DoneOrderRepository doneOrderRepository;
    private final DoneOrderMenuRepository doneOrderMenuRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public Map<String, Object> getDoneOrderByUserPk(Paging p) {
        long userPk = authenticationFacade.getLoginUserPk();

        Integer totalElements = doneOrderRepository.countByUserPk(userPk);
        Integer totalPage = (totalElements / p.getSize()) + 1;

        List<DoneOrderMiniGetResUser> result = doneOrderRepository.findDoneOrdersByUserPk(userPk, p.getStartIndex(), p.getSize());

        for (DoneOrderMiniGetResUser item : result) {
            item.setReviewState(doneOrderRepository.countReviewsByDoneOrderPk(item.getDoneOrderPk()) == 0 ? 0 : 1);
            List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

    @Transactional
    public Map<String, Object> getDoneOrderByResPk(Long resPk, Paging p) {
        Integer totalElements = doneOrderRepository.countByResPk(resPk);
        Integer totalPage = totalElements % p.getSize() == 0 ? totalElements / p.getSize() : (totalElements / p.getSize()) + 1;

        DoneOrderByResPkDto dto = DoneOrderByResPkDto.builder()
                .resPk(resPk)
                .page(p.getPage())
                .size(p.getSize())
                .startIndex(p.getStartIndex())
                .build();

        List<DoneOrderMiniGetRes> result = doneOrderRepository.findDoneOrdersByResPk(dto.getResPk(), dto.getStartIndex(), dto.getSize());

        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

    @Transactional
    public Map<String, Object> getCancelOrderByResPk(Long resPk, Paging p) {
        Integer totalElements = doneOrderRepository.countCancelOrdersByResPk(resPk);
        Integer totalPage = totalElements % p.getSize() == 0 ? totalElements / p.getSize() : (totalElements / p.getSize()) + 1;

        DoneOrderByResPkDto dto = DoneOrderByResPkDto.builder()
                .resPk(resPk)
                .page(p.getPage())
                .size(p.getSize())
                .startIndex(p.getStartIndex())
                .build();

        List<DoneOrderMiniGetRes> result = doneOrderRepository.findCancelOrdersByResPk(dto.getResPk(), dto.getStartIndex(), dto.getSize());

        for (DoneOrderMiniGetRes item : result) {
            List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
            item.setMenuInfoDtos(result2);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("maxPage", totalPage);
        resultMap.put("contents", result);

        return resultMap;
    }

    @Transactional
    public DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk) {
        long userPk = authenticationFacade.getLoginUserPk();

        Long doneOrderResUser = doneOrderRepository.getDoneOrderResUser(doneOrderPk);
        Long doneOrderUser = doneOrderRepository.getDoneOrderUser(doneOrderPk);

        if (userPk != doneOrderResUser && userPk != doneOrderUser) {
            throw new IllegalArgumentException("주문과 관련된 유저만 열람 가능합니다.");
        }

        DoneOrder doneOrder = doneOrderRepository.findByDoneOrderPk(doneOrderPk);
        if (doneOrder == null) {
            throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다.");
        }

        DoneOrderGetRes result = new DoneOrderGetRes();
        result.setDoneOrderPk(doneOrder.getDoneOrderPk());
        result.setUserPk(doneOrder.getUserPk().getUserPk());
        result.setResPk(doneOrder.getResPk().getSeq());
        result.setResName(doneOrder.getResPk().getName());
        result.setOrderAddress(doneOrder.getOrderAddress());
        result.setOrderPhone(doneOrder.getOrderPhone());
        result.setOrderPrice(doneOrder.getOrderPrice());
        result.setOrderRequest(doneOrder.getOrderRequest());
        result.setDoneOrderState(doneOrder.getDoneOrderState());
        result.setPaymentMethod(doneOrder.getPaymentMethod());
        result.setCreatedAt(doneOrder.getCreatedAt());

        List<MenuInfoDto> menuInfoList = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(doneOrderPk);
        result.setMenuInfoList(menuInfoList);

        return result;
    }

}
