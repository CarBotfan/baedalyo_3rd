package com.green.beadalyo.lmy.doneorder.repository;

import com.green.beadalyo.lmy.doneorder.entity.DoneOrderMenu;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.model.MenuInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoneOrderMenuRepository extends JpaRepository<DoneOrderMenu, Long> {
    @Query("SELECT new com.green.beadalyo.lmy.order.model.MenuInfoDto(m.menuName, m.menuPrice) FROM DoneOrderMenu m WHERE m.doneOrderPk.doneOrderPk = :doneOrderPk")
    List<MenuInfoDto> findMenuInfoByDoneOrderPk(@Param("doneOrderPk") Long doneOrderPk);

}
