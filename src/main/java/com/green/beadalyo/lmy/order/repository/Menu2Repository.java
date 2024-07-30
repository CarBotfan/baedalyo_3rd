package com.green.beadalyo.lmy.order.repository;

import com.green.beadalyo.lmy.order.entity.Menu2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Menu2Repository extends JpaRepository<Menu2, Long> {
    List<Menu2> findByMenuPkIn(List<Long> menuPkList);
}
