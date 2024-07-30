package com.green.beadalyo.lmy.doneorder.repository;

import com.green.beadalyo.lmy.doneorder.entity.DoneOrderMenu;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoneOrderMenuRepository extends JpaRepository<DoneOrderMenu, Long> {

}
