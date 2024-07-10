package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    List<Map<String, Object>> selectMenus(List<Long> menuPkList);
    void postOrderTable(OrderPostReq orderPostReq);
    void insertOrderMenuBatch(List<Map<String, Object>> orderMenuList);

    OrderEntity selectOrderById(Long orderPk);
    List<OrderMenuEntity> selectOrderMenusById(Long orderPk);
    void insertDoneOrder(OrderEntity orderEntity);
    void insertDoneOrderMenu(List<OrderMenuEntity> orderMenuEntities);
    void deleteOrder(Long orderPk);

    List<OrderMiniGetRes> selectOrdersByUserPk(Long userPk);
    List<OrderMiniGetRes> selectNonConfirmOrdersByResPk(Long resPk);
    List<OrderMiniGetRes> selectConfirmOrdersByResPk(Long resPk);
    List<String> selectMenuNames(Long orderPk);

    OrderGetRes getOrderInfo(Long orderPk);
    List<MenuInfoDto> selectMenuInfo(Long orderPk);

    void confirmOrder(Long orderPk);

    Long getResUserPkByOrderPk(Long orderPk);
    Long getResUserPkByResPk(Long resPk);
}
