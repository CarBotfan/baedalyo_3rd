package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.model.*;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderMenuRepository;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoneOrderService {
    private final DoneOrderRepository doneOrderRepository;
    private final DoneOrderMenuRepository doneOrderMenuRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @Transactional
    public Map<String, Object> getDoneOrderByUserPk(Paging p) {
        long userPk = authenticationFacade.getLoginUserPk();

        Pageable pageable = PageRequest.of(p.getPage(), p.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DoneOrderMiniGetResUser> result = doneOrderRepository.findDoneOrdersByUserPk(userPk, pageable);

        Map<String, Object> resultMap = new HashMap<>();

        if (result != null) {
            for (DoneOrderMiniGetResUser item : result.getContent()) {
                item.setReviewState(doneOrderRepository.countReviewsByDoneOrderPk(item.getDoneOrderPk()) == 0 ? 0 : 1);
                List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
                item.setMenuInfoDtos(result2);
            }

            resultMap.put("maxPage", result.getTotalPages());
            resultMap.put("contents", result.getContent());
        }

        return resultMap;
    }

    @Transactional
    public Map<String, Object> getDoneOrderByResPk(Long resPk, Paging p) {

        Pageable pageable = PageRequest.of(p.getPage(), p.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DoneOrderMiniGetRes> result = doneOrderRepository.findDoneOrdersByResPk(resPk, pageable);

        Map<String, Object> resultMap = new HashMap<>();

        if (result != null) {
            for (DoneOrderMiniGetRes item : result.getContent()) {
                List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
                item.setMenuInfoDtos(result2);
            }

            resultMap.put("maxPage", result.getTotalPages());
            resultMap.put("contents", result.getContent());
        }

        return resultMap;
    }

    @Transactional
    public Map<String, Object> getCancelOrderByResPk(Long resPk, Paging p) {

        Pageable pageable = PageRequest.of(p.getPage(), p.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DoneOrderMiniGetRes> result = doneOrderRepository.findCancelOrdersByResPk(resPk, pageable);

        Map<String, Object> resultMap = new HashMap<>();

        if (result != null) {
            for (DoneOrderMiniGetRes item : result.getContent()) {
                List<MenuInfoDto> result2 = doneOrderMenuRepository.findMenuInfoByDoneOrderPk(item.getDoneOrderPk());
                item.setMenuInfoDtos(result2);
            }

            resultMap.put("maxPage", result.getTotalPages());
            resultMap.put("contents", result.getContent());
        }

        return resultMap;
    }

    @Transactional
    public DoneOrderGetRes getDoneOrderInfo(Long doneOrderPk) {
        long userPk = authenticationFacade.getLoginUserPk();

        DoneOrder doneOrder1 = doneOrderRepository.getReferenceById(doneOrderPk);

        if (userPk != doneOrder1.getResPk().getUser().getUserPk()
                && userPk != doneOrder1.getUserPk().getUserPk()) {
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