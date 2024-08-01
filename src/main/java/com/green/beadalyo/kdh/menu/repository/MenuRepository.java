package com.green.beadalyo.kdh.menu.repository;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    @Modifying
    @Query("update MenuEntity me set me.menuPic = :menuPic where me.menuPk = :menuPk")
    MenuEntity updateMenuPic(String menuPic, Long menuPk);



}
