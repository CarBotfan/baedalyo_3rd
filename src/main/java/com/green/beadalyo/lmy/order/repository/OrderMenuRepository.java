package com.green.beadalyo.lmy.order.repository;


import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
    List<OrderMenu> findOrderMenusByOrder(Order orderPk);
    List<OrderMenu> findByOrder_OrderPk(Long orderPk);

    @Query("SELECT om.menuName FROM OrderMenu om WHERE om.order.orderPk = :orderPk")
    List<String> findMenuNamesByOrderPk(@Param("orderPk") Long orderPk);

    @Query("SELECT new com.green.beadalyo.lmy.order.model.MenuInfoDto(om.menuName, om.menuPrice) " +
            "FROM OrderMenu om WHERE om.order.orderPk = :orderPk")
    List<MenuInfoDto> findMenuInfoByOrderPk(@Param("orderPk") Long orderPk);
}
