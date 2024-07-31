package com.green.beadalyo.lmy.doneorder.repository;

import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetResUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoneOrderRepository extends JpaRepository<DoneOrder, Long> {
    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetResUser(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt, NULL) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.userPk.userPk = :userPk ORDER BY o.createdAt DESC")
    List<DoneOrderMiniGetResUser> findDoneOrdersByUserPk(@Param("userPk") Long userPk, @Param("startIndex") int startIndex, @Param("size") int size);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.userPk.userPk = :userPk")
    Integer countByUserPk(@Param("userPk") Long userPk);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.doneOrderPk = :doneOrderPk")
    Integer countReviewsByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk ORDER BY o.createdAt DESC")
    List<DoneOrderMiniGetRes> findDoneOrdersByResPk(@Param("resPk") Long resPk, @Param("startIndex") int startIndex, @Param("size") int size);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 1")
    Integer countByResPk(@Param("resPk") Long resPk);

    @Query("SELECT new com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes(o.doneOrderPk, r.seq, r.pic, r.name, o.orderPrice, o.doneOrderState, o.createdAt) " +
            "FROM DoneOrder o JOIN Restaurant r ON o.resPk.seq = r.seq WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2 ORDER BY o.createdAt DESC")
    List<DoneOrderMiniGetRes> findCancelOrdersByResPk(@Param("resPk") Long resPk, @Param("startIndex") int startIndex, @Param("size") int size);

    @Query("SELECT COUNT(o) FROM DoneOrder o WHERE o.resPk.seq = :resPk AND o.doneOrderState = 2")
    Integer countCancelOrdersByResPk(@Param("resPk") Long resPk);

    @Query("SELECT o FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    DoneOrder findByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT o.userPk.userPk FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk")
    Long getDoneOrderUser(@Param("doneOrderPk") Long doneOrderPk);

    @Query("SELECT r.user FROM Restaurant r WHERE r.seq = (SELECT o.resPk.seq FROM DoneOrder o WHERE o.doneOrderPk = :doneOrderPk)")
    Long getDoneOrderResUser(@Param("doneOrderPk") Long doneOrderPk);
}
