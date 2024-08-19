package com.green.beadalyo.lmy.order.repository;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByOrderUserPk_UserPkOrderByCreatedAtDesc(Long userPk);
    List<Order> findByOrderUser(User user) ;
    List<Order> findByOrderResAndOrderState(Restaurant rest, Integer state) ;


//    @Query("SELECT new com.green.beadalyo.lmy.order.model.OrderMiniGetRes(o.orderPk, o.orderRes.seq, r.pic, r.name, " +
//            "o.orderPrice, o.orderState, o.createdAt) " +
//            "FROM Order o " +
//            "JOIN Restaurant r ON o.orderRes.seq = r.seq " +
//            "WHERE o.orderRes.seq = :resPk AND o.orderState = 1 " +
//            "ORDER BY o.createdAt DESC")
//    List<OrderMiniGetRes> findNonConfirmOrdersByResPk(@Param("resPk") Long resPk);

//    @Query("SELECT new com.green.beadalyo.lmy.order.model.OrderMiniGetRes(o.orderPk, o.orderRes.seq, r.pic, r.name, " +
//            "o.orderPrice, o.orderState, o.createdAt) " +
//            "FROM Order o " +
//            "JOIN Restaurant r ON o.orderRes.seq = r.seq " +
//            "WHERE o.orderRes.seq = :resPk AND o.orderState = 2 " +
//            "ORDER BY o.createdAt DESC")
//    List<OrderMiniGetRes> findConfirmOrdersByResPk(@Param("resPk") Long resPk);

//    @Query("SELECT new com.green.beadalyo.lmy.order.model.OrderGetRes(o.orderPk, u.userPk, r.seq, r.name, " +
//            "o.orderAddress, o.orderPhone, o.orderPrice, o.orderRequest, o.orderState, o.paymentMethod, o.createdAt) " +
//            "FROM Order o " +
//            "JOIN o.orderUserPk u " +
//            "JOIN o.orderRes r " +
//            "WHERE o.orderPk = :orderPk")
//    OrderGetRes getOrderInfo(@Param("orderPk") Long orderPk);

    Optional<Order> findByOrderPk(Long orderPk);

        @Modifying
        @Transactional
        @Query("UPDATE Order o SET o.orderState = 2 WHERE o.orderPk = :orderPk")
    int confirmOrder(@Param("orderPk") Long orderPk);
}
