package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;


    @Transactional
    public int postOrder(OrderPostReq p) {
        if (500 < p.getOrderRequest().length()) {
            throw new RuntimeException("요청사항 양식 안맞음(글자 수)");
        }

        List<Map<String, Object>> menuList = orderMapper.selectMenus(p.getMenuPk());

        // 총 가격 계산
        int totalPrice = p.getMenuPk().stream()
                .mapToInt(menuPk -> menuList.stream()
                        .filter(menu -> menuPk.equals(menu.get("menu_pk")))
                        .mapToInt(menu -> (Integer) menu.get("menu_price"))
                        .sum())
                .sum();

        p.setOrderPrice(totalPrice);

        try {
            orderMapper.postOrderTable(p);
        } catch (Exception e) {
            throw new RuntimeException("order 쿼링 이슈");
        }

        // order_menu에 삽입할 데이터 리스트 생성
        List<Map<String, Object>> orderMenuList = null;
        try {
            orderMenuList = p.getMenuPk().stream().map(menuPk -> {
                Map<String, Object> menu = menuList.stream()
                        .filter(m -> m.get("menu_pk").equals(menuPk))
                        .findFirst().orElseThrow(() -> new RuntimeException("Menu not found"));
                Map<String, Object> orderMenuMap = new HashMap<>();
                orderMenuMap.put("orderPk", p.getOrderPk());
                orderMenuMap.put("menuPk", menu.get("menu_pk"));
                orderMenuMap.put("menuName", menu.get("menu_name"));
                orderMenuMap.put("menuPrice", menu.get("menu_price"));
                return orderMenuMap;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("MAP 오류");
        }

        // order_menu 테이블에 배치 삽입
        try {
            orderMapper.insertOrderMenuBatch(orderMenuList);
        } catch (Exception e) {
            throw new RuntimeException("orderMenu 쿼링 이슈");
        }

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
        try {
            menuEntities = orderMapper.selectOrderMenusById(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("주문메뉴 불러오기 오류");
        }

        try {
            entity.setOrderState(2);
            orderMapper.insertDoneOrder(entity);
            for (OrderMenuEntity orderMenuEntity : menuEntities) {
                orderMenuEntity.setDoneOrderPk(entity.getDoneOrderPk());
            }
            orderMapper.insertDoneOrderMenu(menuEntities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("취소 삽입 오류");
        }

        try {
            orderMapper.deleteOrder(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("마무리 오류");
        }

        return 1;
    }

    @Transactional
    public int completeOrder(Long orderPk) {
        OrderEntity entity = null;
        try {
            entity = orderMapper.selectOrderById(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("주문 불러오기 오류");
        }

        List<OrderMenuEntity> menuEntities = null;
        try {
            menuEntities = orderMapper.selectOrderMenusById(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("주문메뉴 불러오기 오류");
        }

        try {
            entity.setOrderState(1);
            orderMapper.insertDoneOrder(entity);
            for (OrderMenuEntity orderMenuEntity : menuEntities) {
                orderMenuEntity.setDoneOrderPk(entity.getDoneOrderPk());
            }
            orderMapper.insertDoneOrderMenu(menuEntities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("완료 삽입 오류");
        }

        try {
            orderMapper.deleteOrder(orderPk);
        } catch (Exception e) {
            throw new RuntimeException("마무리 오류");
        }

        return 1;
    }

    public List<OrderMiniGetRes> getUserOrderList(Long userPk){
        List<OrderMiniGetRes> result = null;
        try {
            result = orderMapper.selectOrdersByUserPk(userPk);
            for (OrderMiniGetRes item : result) {
                List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
                item.setMenuName(result2);
            }
        } catch (Exception e) {
            throw new RuntimeException("불러오기 오류");
        }

        return result;
    }

    public List<OrderMiniGetRes> getResOrderList(Long resPk){
        List<OrderMiniGetRes> result = null;
        try {
            result = orderMapper.selectOrdersByResPk(resPk);
            for (OrderMiniGetRes item : result) {
                List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
                item.setMenuName(result2);
            }
        } catch (Exception e) {
            throw new RuntimeException("불러오기 오류");
        }

        return result;
    }

    public OrderGetRes getOrderInfo(Long orderPk) {
        OrderGetRes result = null;
        try {
            result = orderMapper.getOrderInfo(orderPk);
            result.setMenuInfoList(orderMapper.selectMenuInfo(orderPk));
        } catch (Exception e) {
            throw new RuntimeException("불러오기 오류");
        }

        return result;
    }
}
