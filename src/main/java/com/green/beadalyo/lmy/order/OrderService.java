package com.green.beadalyo.lmy.order;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository2;
import com.green.beadalyo.lmy.order.entity.Menu2;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.model.*;
import com.green.beadalyo.lmy.order.repository.Menu2Repository;
import com.green.beadalyo.lmy.order.repository.OrderMenuRepository;
import com.green.beadalyo.lmy.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final Menu2Repository menuRepository;
    private final UserRepository2 userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthenticationFacade authenticationFacade;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Post Orderㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public List<Map<String, Object>> getMenuDetails(List<Long> menuPkList) {
        List<Menu2> menus = menuRepository.findByMenuPkIn(menuPkList);
        return menus.stream()
                .map(menu -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menu_pk", menu.getMenuPk());
                    map.put("menu_name", menu.getMenuName());
                    map.put("menu_price", menu.getMenuPrice());
                    return map;
                })
                .collect(Collectors.toList());
    }


    public int calculateTotalPrice(List<Long> menuPkList) {
        List<Menu2> menus = menuRepository.findByMenuPkIn(menuPkList);
        return menus.stream()
                .filter(menu -> menuPkList.contains(menu.getMenuPk()))
                .mapToInt(Menu2::getMenuPrice)
                .sum();
    }


    public Order saveOrder(OrderPostReq p) {
        Order order = new Order();
        order.setOrderUserPk(userRepository.getReferenceById(p.getOrderUserPk()));
        order.setOrderResPk(restaurantRepository.getReferenceById(p.getOrderResPk()));
        order.setOrderRequest(p.getOrderRequest());
        order.setPaymentMethod(p.getPaymentMethod());
        order.setOrderPhone(p.getOrderPhone());
        order.setOrderAddress(p.getOrderAddress());
        order.setOrderPrice(p.getOrderPrice());
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }


    public List<Map<String, Object>> createOrderMenuList(OrderPostReq p, List<Map<String, Object>> menuList) {
        return p.getMenuPk().stream().map(menuPk -> {
            Map<String, Object> menu = menuList.stream()
                    .filter(m -> m.get("menu_pk").equals(menuPk))
                    .findFirst().orElseThrow(() -> new NullPointerException(""));
            Map<String, Object> orderMenuMap = new HashMap<>();
            orderMenuMap.put("orderPk", p.getOrderPk());
            orderMenuMap.put("menuPk", menu.get("menu_pk"));
            orderMenuMap.put("menuName", menu.get("menu_name"));
            orderMenuMap.put("menuPrice", menu.get("menu_price"));
            return orderMenuMap;
        }).collect(Collectors.toList());
    }

    public void saveOrderMenuBatch(List<Map<String, Object>> orderMenuList, Order order) {
        List<OrderMenu> orderMenus = orderMenuList.stream()
                .map(menuMap -> {
                    OrderMenu orderMenu = new OrderMenu();
                    orderMenu.setOrderPk(order);
                    orderMenu.setMenuPk(menuRepository.getReferenceById((Long) menuMap.get("menuPk")));
                    orderMenu.setMenuName((String) menuMap.get("menuName"));
                    orderMenu.setMenuPrice((Integer) menuMap.get("menuPrice"));
                    orderMenu.setCreatedAt(LocalDateTime.now());
                    return orderMenu;
                })
                .collect(Collectors.toList());
        orderMenuRepository.saveAll(orderMenus);
    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Cancel Orderㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @Transactional
    public int cancelOrder(Long orderPk) {

        long userPk = authenticationFacade.getLoginUserPk();

        OrderEntity entity = null;
        try {
            entity = orderMapper.selectOrderById(orderPk);
            entity.setCanceller(orderMapper.selectCancellerRole(userPk));
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
            throw new RuntimeException("");
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
            throw new RuntimeException();
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
            throw new RuntimeException();
        }

        List<OrderMiniGetRes> result = orderMapper.selectConfirmOrdersByResPk(resPk);

        for (OrderMiniGetRes item : result) {
            List<String> result2 = orderMapper.selectMenuNames(item.getOrderPk());
            item.setMenuName(result2);
        }

        return result;
    }

    public OrderGetRes getOrderInfo(Long orderPk) {

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderMapper.getOrderResUser(orderPk)
        && userPk != orderMapper.getOrderUser(orderPk)) {
            throw new IllegalArgumentException("주문과 관련된 유저만 열람 가능합니다.");
        }

        OrderGetRes result = orderMapper.getOrderInfo(orderPk);
        result.setMenuInfoList(orderMapper.selectMenuInfo(orderPk));

        return result;
    }

    public Integer confirmOrder(Long orderPk) {

        long resUserPk = authenticationFacade.getLoginUserPk();

        if (resUserPk != orderMapper.getResUserPkByOrderPk(orderPk)) {
            throw new RuntimeException("");
        }

        orderMapper.confirmOrder(orderPk);

        return 1;
    }
}
