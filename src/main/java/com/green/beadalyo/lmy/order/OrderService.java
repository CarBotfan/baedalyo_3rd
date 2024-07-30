package com.green.beadalyo.lmy.order;

import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository2;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrderMenu;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderMenuRepository;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
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
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final DoneOrderRepository doneOrderRepository;
    private final DoneOrderMenuRepository doneOrderMenuRepository;
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


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Cancel & Complete Orderㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public Order getOrderEntity(Long orderPk) {
        return orderRepository.getReferenceById(orderPk);
    }


    public void setCanceller(DoneOrder doneOrder, long userPk) {
        doneOrder.setCanceller(userRepository.getReferenceById(userPk).getUserRole());
    }


    public List<OrderMenu> getOrderMenuEntities(Long orderPk) {
        return orderMenuRepository
                .findOrderMenusByOrderPk(orderRepository.getReferenceById(orderPk));
    }


    public DoneOrder saveDoneOrder(Order order, long userPk, Integer isDone) {
        DoneOrder doneOrder = new DoneOrder();
        doneOrder.setUserPk(order.getOrderUserPk());
        doneOrder.setResPk(order.getOrderResPk());
        doneOrder.setOrderPrice(order.getOrderPrice());
        doneOrder.setOrderRequest(order.getOrderRequest());
        doneOrder.setOrderPhone(order.getOrderPhone());
        doneOrder.setOrderAddress(order.getOrderAddress());
        doneOrder.setPaymentMethod(order.getPaymentMethod());
        doneOrder.setOrderMethod(order.getOrderMethod());
        doneOrder.setDoneOrderState(isDone);
        if (isDone == 2) {setCanceller(doneOrder, userPk);}
        return doneOrderRepository.save(doneOrder);
    }

    public void saveDoneOrderMenuBatch(List<OrderMenu> menus, DoneOrder doneOrder) {
        List<DoneOrderMenu> doneOrderMenus = menus.stream()
                .map(menu -> {
                    DoneOrderMenu doneOrderMenu = new DoneOrderMenu();
                    doneOrderMenu.setDoneOrderPk(doneOrder);
                    doneOrderMenu.setMenuPk(menu.getMenuPk());
                    doneOrderMenu.setMenuName(menu.getMenuName());
                    doneOrderMenu.setMenuPrice(menu.getMenuPrice());
                    return doneOrderMenu;
                })
                .collect(Collectors.toList());
        doneOrderMenuRepository.saveAll(doneOrderMenus);
    }

    public void deleteOrder(Long orderPk) {
        orderRepository.deleteById(orderPk);
    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Get Order + @ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


    @Transactional
    public List<OrderMiniGetRes> getUserOrderList() {
        long userPk = authenticationFacade.getLoginUserPk();
        List<Order> orders = orderRepository.findByOrderUserPk_UserPkOrderByCreatedAtDesc(userPk);

        return orders.stream().map(order -> {
            OrderMiniGetRes res = new OrderMiniGetRes();
            res.setOrderPk(order.getOrderPk());
            res.setResPk(order.getOrderResPk().getSeq());
            res.setResPic(order.getOrderResPk().getPic());
            res.setResName(order.getOrderResPk().getName());
            res.setOrderPrice(order.getOrderPrice());
            res.setOrderState(order.getOrderState());
            res.setCreatedAt(order.getCreatedAt());

            List<String> menuNames = orderMenuRepository.findByOrderPk(order)
                    .stream()
                    .map(OrderMenu::getMenuName)
                    .collect(Collectors.toList());
            res.setMenuName(menuNames);

            return res;
        }).collect(Collectors.toList());
    }


    public List<OrderMiniGetRes> getResNonConfirmOrderList(Long resPk) {
        List<OrderMiniGetRes> result = orderRepository.findNonConfirmOrdersByResPk(resPk);

        for (OrderMiniGetRes item : result) {
            List<String> menuNames = orderMenuRepository.findMenuNamesByOrderPk(item.getOrderPk());
            item.setMenuName(menuNames);
        }

        return result;
    }

    public List<OrderMiniGetRes> getResConfirmOrderList(Long resPk){

        List<OrderMiniGetRes> result = orderRepository.findConfirmOrdersByResPk(resPk);

        for (OrderMiniGetRes item : result) {
            List<String> menuNames = orderMenuRepository.findMenuNamesByOrderPk(item.getOrderPk());
            item.setMenuName(menuNames);
        }

        return result;
    }

    public OrderGetRes getOrderInfo(Long orderPk) {

        OrderGetRes result = orderRepository.getOrderInfo(orderPk);
        List<MenuInfoDto> menuInfoList = orderMenuRepository.findMenuInfoByOrderPk(orderPk);
        result.setMenuInfoList(menuInfoList);

        return result;
    }

    public Integer confirmOrder(Long orderPk) {

        orderRepository.confirmOrder(orderPk);

        return 1;
    }
}
