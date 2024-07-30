package com.green.beadalyo.lmy.order.repository;

import com.green.beadalyo.lmy.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
