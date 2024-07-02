package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.OrderEntity;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMenuEntity;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
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

    List<OrderGetRes> selectOrdersByUserPk(Long userPk);
    List<OrderGetRes> selectOrdersByResPk(Long resPk);
    List<String> selectMenuNames(Long orderPk);
}
