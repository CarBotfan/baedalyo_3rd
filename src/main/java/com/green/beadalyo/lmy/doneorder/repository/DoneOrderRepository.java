package com.green.beadalyo.lmy.doneorder.repository;

import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoneOrderRepository extends JpaRepository<DoneOrder, Long> {
    @Query(value = "SELECT " +
            "o.doneOrderPk , r.seq as resPK, r.pic as resPic, r.name as resName, o.orderPrice as orderPrice, o.doneOrderState as doneOrderState, o.createdAt as reatedAt, NULL " +
            "FROM DoneOrder o JOIN o.resPk r " +
            "WHERE o.userPk.userPk = :userPk")
    Page<DoneOrderMiniGetResUser> findDoneOrdersByUserPk(@Param("userPk") Long userPk, Pageable pageable);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.userPk.userPk = :userPk")
    Integer countByUserPk(@Param("userPk") Long userPk);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.doneOrderPk = :doneOrderPk")
    Integer countReviewsByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query(value = "SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk AND o.doneOrderState = 1",
            countQuery = "SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 1")
    Page<DoneOrderMiniGetRes> findDoneOrdersByResPk(@Param("resPk") Long resPk, Pageable pageable);

    @Query(value = "SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2",
            countQuery = "SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2")
    Page<DoneOrderMiniGetRes> findCancelOrdersByResPk(@Param("resPk") Long resPk, Pageable pageable);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2")
    Integer countCancelOrdersByResPk(@Param("resPk") Long resPk);

    @Query("SELECT o FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    DoneOrder findByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT o.userPk.userPk FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    Long getDoneOrderUser(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT r.user FROM Restaurant r WHERE r.seq = (SELECT o.resPk.seq FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk)")
    Long getDoneOrderResUser(@Param("doneOrderPk") Long doneOrderPk);

    @Query(value = "SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS createdAt, SUM(o.order_price) AS monthSales " +
            "FROM done_order o WHERE DATE_FORMAT(o.created_at, '%Y') = :date AND o.res_pk = :resPk AND o.done_order_state = 1 " +
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')", nativeQuery = true)
    List<MonthSalesDto> getMonthSales(@Param("date") String date, @Param("resPk") Long resPk);

    @Query(value = "SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS createdAt, COUNT(o.done_order_pk) AS monthOrderCount " +
            "FROM done_order o WHERE DATE_FORMAT(o.created_at, '%Y') = :date AND o.res_pk = :resPk AND o.done_order_state = 1 " +
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')", nativeQuery = true)
    List<MonthOrderCountDto> getMonthOrderCount(@Param("date") String date, @Param("resPk") Long resPk);

    @Query(value = "SELECT DATE_FORMAT(o.created_at, '%Y-%m-%d') AS createdAt, SUM(o.order_price) AS dailySales " +
            "FROM done_order o WHERE DATE_FORMAT(o.created_at, '%Y-%m') = :date AND o.res_pk = :resPk AND o.done_order_state = 1 " +
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m-%d')", nativeQuery = true)
    List<DailySalesDto> getDailySales(@Param("date") String date, @Param("resPk") Long resPk);

    @Query(value = "SELECT DATE_FORMAT(o.created_at, '%Y-%m-%d') AS createdAt, COUNT(o.done_order_pk) AS dailyOrderCount " +
            "FROM done_order o WHERE DATE_FORMAT(o.created_at, '%Y-%m') = :date AND o.res_pk = :resPk AND o.done_order_state = 1 " +
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m-%d')", nativeQuery = true)
    List<DailyOrderCountDto> getDailyOrderCount(@Param("date") String date, @Param("resPk") Long resPk);


}
