package com.green.beadalyo.lmy.order;

import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.repository.MenuRepository;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderMenuRepository;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.repository.OrderMenuRepository;
import com.green.beadalyo.lmy.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡValidation 전용ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public Order getOrderByOrderPk(long orderPk){
        return orderRepository.getReferenceById(orderPk);
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡPost Orderㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

//    public List<Map<String, Object>> getMenuDetails(List<Long> menuPkList) {
//        List<MenuEntity> menus = menuRepository.findByMenuPkIn(menuPkList);
//        return menus.stream()
//                .map(menu -> {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("menu_pk", menu.getMenuPk());
//                    map.put("menu_name", menu.getMenuName());
//                    map.put("menu_price", menu.getMenuPrice());
//                    return map;
//                })
//                .collect(Collectors.toList());
//    }


//    public int calculateTotalPrice(List<Long> menuPkList) {
//        List<MenuEntity> menus = menuRepository.findByMenuPkIn(menuPkList);
//        return menus.stream()
//                .filter(menu -> menuPkList.contains(menu.getMenuPk()))
//                .mapToInt(MenuEntity::getMenuPrice)
//                .sum();
//    }


//    public Order saveOrder(OrderPostReq p) {
//        Order order = new Order();
//        order.setOrderUserPk(userRepository.getReferenceById(p.getOrderUserPk()));
//        order.setOrderResPk(restaurantRepository.getReferenceById(p.getOrderResPk()));
//        order.setOrderRequest(p.getOrderRequest());
//        order.setPaymentMethod(p.getPaymentMethod());
//        order.setOrderPhone(p.getOrderPhone());
//        order.setOrderAddress(p.getOrderAddress());
//        order.setOrderPrice(p.getOrderPrice());
//        order.setOrderState(1);
//        //이거는 포장주문이면 1들어갈 수 있도록 바꿔야함.
//        order.setOrderMethod(0);
//        return orderRepository.save(order);
//    }

    public void saveOrder(Order order) throws Exception
    {
        orderRepository.save(order);
    }

    public void deleteOrder(Order order)
    {
        orderRepository.delete(order);
    }

//    public List<Map<String, Object>> createOrderMenuList(OrderPostReq p, List<Map<String, Object>> menuList) {
//        return p.getMenuPk().stream().map(menuPk -> {
//            Map<String, Object> menu = menuList.stream()
//                    .filter(m -> m.get("menu_pk").equals(menuPk))
//                    .findFirst().orElseThrow(() -> new NullPointerException(""));
//            Map<String, Object> orderMenuMap = new HashMap<>();
//            orderMenuMap.put("orderPk", p.getOrderPk());
//            orderMenuMap.put("menuPk", menu.get("menu_pk"));
//            orderMenuMap.put("menuName", menu.get("menu_name"));
//            orderMenuMap.put("menuPrice", menu.get("menu_price"));
//            return orderMenuMap;
//        }).collect(Collectors.toList());
//    }

//    public void saveOrderMenuBatch(List<Map<String, Object>> orderMenuList, Order order) {
//        List<OrderMenu> orderMenus = orderMenuList.stream()
//                .map(menuMap -> {
//                    OrderMenu orderMenu = new OrderMenu();
//                    orderMenu.setOrderPk(order);
//                    orderMenu.setMenuPk(menuRepository.getReferenceById((Long) menuMap.get("menuPk")));
//                    orderMenu.setMenuName((String) menuMap.get("menuName"));
//                    orderMenu.setMenuPrice((Integer) menuMap.get("menuPrice"));
//                    orderMenu.setCreatedAt(LocalDateTime.now());
//                    return orderMenu;
//                })
//                .collect(Collectors.toList());
//        orderMenuRepository.saveAll(orderMenus);
//    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Cancel & Complete Orderㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public Order getOrderEntity(Long orderPk) {
        return orderRepository.getReferenceById(orderPk);
    }


    public void setCanceller(DoneOrder doneOrder, long userPk) {
        doneOrder.setCanceller(userRepository.getReferenceById(userPk).getUserRole());
    }


    public List<OrderMenu> getOrderMenuEntities(Long orderPk) {
        return orderMenuRepository
                .findOrderMenusByOrder(orderRepository.getReferenceById(orderPk));
    }


    public void saveDoneOrder(Order order, long userPk, Integer isDone) {
        DoneOrder doneOrder = new DoneOrder(order);
        doneOrder.setDoneOrderState(isDone);
        if (isDone == 2) setCanceller(doneOrder, userPk);
        doneOrderRepository.save(doneOrder);
    }

//    public void saveDoneOrderMenuBatch(List<OrderMenu> menus, DoneOrder doneOrder) {
//        List<DoneOrderMenu> doneOrderMenus = menus.stream()
//                .map(menu -> {
//                    DoneOrderMenu doneOrderMenu = new DoneOrderMenu();
//                    doneOrderMenu.setDoneOrderPk(doneOrder);
//                    doneOrderMenu.setMenuPk(menu.getMenuPk());
//                    doneOrderMenu.setMenuName(menu.getMenuName());
//                    doneOrderMenu.setMenuPrice(menu.getMenuPrice());
//                    return doneOrderMenu;
//                })
//                .collect(Collectors.toList());
//        doneOrderMenuRepository.saveAll(doneOrderMenus);
//    }

    public void deleteOrder(Long orderPk) {
        orderRepository.deleteById(orderPk);
    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Get Order + @ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public List<Order> getOrderListByUser(User user)
    {
        return orderRepository.findByOrderUser(user) ;
    }

    public List<Order> getOrderListByRestaurantAndState(Restaurant rest, Integer state)
    {
        return orderRepository.findByOrderResAndOrderState(rest, state) ;
    }

    public Order getOrderBySeq(Long seq) throws Exception
    {
        return orderRepository.findById(seq).orElseThrow(DataNotFoundException::new) ;
    }

//    @Transactional
//    public List<OrderMiniGetRes> getUserOrderList() {
//        long userPk = authenticationFacade.getLoginUserPk();
//        List<Order> orders = orderRepository.findByOrderUserPk_UserPkOrderByCreatedAtDesc(userPk);
//
//        return orders.stream().map(OrderMiniGetRes::new).collect(Collectors.toList());
//    }
//
//
//    public List<OrderMiniGetRes> getResNonConfirmOrderList(Long resPk) {
//        List<OrderMiniGetRes> result = orderRepository.findNonConfirmOrdersByResPk(resPk);
//        return code(result);
//    }
//
//    public List<OrderMiniGetRes> getResConfirmOrderList(Long resPk){
//
//        List<OrderMiniGetRes> result = orderRepository.findConfirmOrdersByResPk(resPk);
//        return code(result);
//    }
//
//    private List<OrderMiniGetRes> code(List<OrderMiniGetRes> result)
//    {
//        for (OrderMiniGetRes item : result) {
//            List<OrderMenu> menuNames = orderMenuRepository.findByOrder_OrderPk(item.getOrderPk());
//        }
//        return result ;
//    }

    public Order getOrderInfo(Long orderPk) throws Exception {
        return orderRepository.findByOrderPk(orderPk).orElseThrow(NullPointerException::new);
    }

    public int confirmOrder(long orderPk) {
        return orderRepository.confirmOrder(orderPk);
    }



}


