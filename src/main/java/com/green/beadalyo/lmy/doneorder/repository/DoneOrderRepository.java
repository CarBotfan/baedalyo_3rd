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
    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetResUser(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt, NULL) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.userPk.userPk = :userPk")
    Page<DoneOrderMiniGetResUser> findDoneOrdersByUserPk(@Param("userPk") Long userPk, Pageable pageable);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.userPk.userPk = :userPk")
    Integer countByUserPk(@Param("userPk") Long userPk);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.doneOrderPk = :doneOrderPk")
    Integer countReviewsByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk AND o.doneOrderState = 1")
    Page<DoneOrderMiniGetRes> findDoneOrdersByResPk(@Param("resPk") Long resPk, Pageable pageable);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 1")
    Integer countByResPk(@Param("resPk") Long resPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2")
    Page<DoneOrderMiniGetRes> findCancelOrdersByResPk(@Param("resPk") Long resPk, Pageable pageable);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2")
    Integer countCancelOrdersByResPk(@Param("resPk") Long resPk);

    @Query("SELECT o FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    DoneOrder findByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT o.userPk.userPk FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    Long getDoneOrderUser(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT r.user FROM Restaurant r WHERE r.seq = (SELECT o.resPk.seq FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk)")
    Long getDoneOrderResUser(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.MonthSalesDto(DATE_FORMAT(o.createdAt, '%Y-%m'), SUM(o.orderPrice)) " +
            "FROM DoneOrder o WHERE DATE_FORMAT(o.createdAt, '%Y') = :date AND o.resPk = :resPk AND o.doneOrderState = 1 " +
            "GROUP BY DATE_FORMAT(o.createdAt, '%Y-%m')")
    List<MonthSalesDto> getMonthSales(@Param("date") String date, @Param("resPk") Long resPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto(DATE_FORMAT(o.createdAt, '%Y-%m'), COUNT(o.doneOrderPk)) " +
            "FROM DoneOrder o WHERE DATE_FORMAT(o.createdAt, '%Y') = :date AND o.resPk = :resPk AND o.doneOrderState = 1 " +
            "GROUP BY DATE_FORMAT(o.createdAt, '%Y-%m')")
    List<MonthOrderCountDto> getMonthOrderCount(@Param("date") String date, @Param("resPk") Long resPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DailySalesDto(DATE_FORMAT(o.createdAt, '%Y-%m-%d'), SUM(o.orderPrice)) " +
            "FROM DoneOrder o WHERE DATE_FORMAT(o.createdAt, '%Y-%m') = :date AND o.resPk = :resPk AND o.doneOrderState = 1 " +
            "GROUP BY DATE_FORMAT(o.createdAt, '%Y-%m-%d')")
    List<DailySalesDto> getDailySales(@Param("date") String date, @Param("resPk") Long resPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto(DATE_FORMAT(o.createdAt, '%Y-%m-%d'), COUNT(o.doneOrderPk)) " +
            "FROM DoneOrder o WHERE DATE_FORMAT(o.createdAt, '%Y-%m') = :date AND o.resPk = :resPk AND o.doneOrderState = 1 " +
            "GROUP BY DATE_FORMAT(o.createdAt, '%Y-%m-%d')")
    List<DailyOrderCountDto> getDailyOrderCount(@Param("date") String date, @Param("resPk") Long resPk);


}
