package com.green.beadalyo.lmy.order;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.order.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import  static com.green.beadalyo.lmy.dataset.ExceptionMsgDataset.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public int postOrder(OrderPostReq p) {

        p.setOrderUserPk(authenticationFacade.getLoginUserPk());

        List<Map<String, Object>> menuList = orderMapper.selectMenus(p.getMenuPk());

        // 총 가격 계산
        int totalPrice = p.getMenuPk().stream()
                .mapToInt(menuPk -> menuList.stream()
                        .filter(menu -> menuPk.equals(menu.get("menu_pk")))
                        .mapToInt(menu -> (Integer) menu.get("menu_price"))
                        .sum())
                .sum();

        p.setOrderPrice(totalPrice);

        orderMapper.postOrderTable(p);

        // order_menu에 삽입할 데이터 리스트 생성
        List<Map<String, Object>> orderMenuList = null;
        try {
            orderMenuList = p.getMenuPk().stream().map(menuPk -> {
                Map<String, Object> menu = menuList.stream()
                        .filter(m -> m.get("menu_pk").equals(menuPk))
                        .findFirst().orElseThrow(() -> new NullPointerException(MENU_NOT_FOUND_ERROR));
                Map<String, Object> orderMenuMap = new HashMap<>();
                orderMenuMap.put("orderPk", p.getOrderPk());
                orderMenuMap.put("menuPk", menu.get("menu_pk"));
                orderMenuMap.put("menuName", menu.get("menu_name"));
                orderMenuMap.put("menuPrice", menu.get("menu_price"));
                return orderMenuMap;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(DATALIST_FAIL_ERROR);
        }

        // order_menu 테이블에 배치 삽입
        orderMapper.insertOrderMenuBatch(orderMenuList);

        return 1;
    }

    @Transactional
    public int cancelOrder(Long orderPk) {

        OrderEntity entity = null;
        try {
            entity = orderMapper.selectOrderById(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("주문 불러오기 오류");
        }

        List<OrderMenuEntity> menuEntities = null;

        menuEntities = orderMapper.selectOrderMenusById(orderPk);


        entity.setOrderState(2);
        orderMapper.insertDoneOrder(entity);
        for (OrderMenuEntity orderMenuEntity : menuEntities) {
            orderMenuEntity.setDoneOrderPk(entity.getDoneOrderPk());
        }
        orderMapper.insertDoneOrderMenu(menuEntities);


        orderMapper.deleteOrder(orderPk);


        return 1;
    }

    @Transactional
    public int completeOrder(Long orderPk) {

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderMapper.getResUserPkByOrderPk(orderPk)) {
            throw new RuntimeException(NO_AUTHENTICATION);
        }

        OrderEntity entity = orderMapper.selectOrderById(orderPk);

        List<OrderMenuEntity> menuEntities = orderMapper.selectOrderMenusById(orderPk);

        entity.setOrderState(1);
        orderMapper.insertDoneOrder(entity);
        for (OrderMenuEntity orderMenuEntity : menuEntities) {
            orderMenuEntity.setDoneOrderPk(entity.getDoneOrderPk());
        }
        orderMapper.insertDoneOrderMenu(menuEntities);

        orderMapper.deleteOrder(orderPk);

        return 1;
    }

    public List<OrderMiniGetRes> getUserOrderList(){
        long userPk = authenticationFacade.getLoginUserPk();
        List<OrderMiniGetRes> result = orderMapper.selectOrdersByUserPk(userPk);

        for (OrderMiniGetRes item : result) {
            List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
            item.setMenuName(result2);
        }

        return result;
    }

    public List<OrderMiniGetRes> getResNonConfirmOrderList(Long resPk){

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderMapper.getResUserPkByResPk(resPk)){
            throw new RuntimeException(NO_AUTHENTICATION);
        }

        List<OrderMiniGetRes> result = orderMapper.selectNonConfirmOrdersByResPk(resPk);

        for (OrderMiniGetRes item : result) {
            List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
            item.setMenuName(result2);
        }

        return result;
    }

    public List<OrderMiniGetRes> getResConfirmOrderList(Long resPk){

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderMapper.getResUserPkByResPk(resPk)){
            throw new RuntimeException(NO_AUTHENTICATION);
        }

        List<OrderMiniGetRes> result = orderMapper.selectConfirmOrdersByResPk(resPk);

        for (OrderMiniGetRes item : result) {
            List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
            item.setMenuName(result2);
        }

        return result;
    }

    public OrderGetRes getOrderInfo(Long orderPk) {

        OrderGetRes result = orderMapper.getOrderInfo(orderPk);
        result.setMenuInfoList(orderMapper.selectMenuInfo(orderPk));

        return result;
    }

    public Integer confirmOrder(Long orderPk) {

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderMapper.getResUserPkByOrderPk(orderPk)) {
            throw new RuntimeException(NO_AUTHENTICATION);
        }

        orderMapper.confirmOrder(orderPk);

        return 1;
    }
}
