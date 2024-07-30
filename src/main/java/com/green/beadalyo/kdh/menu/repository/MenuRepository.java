package com.green.beadalyo.kdh.menu.repository;

import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    MenuEntity
}
