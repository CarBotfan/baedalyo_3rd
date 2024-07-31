package com.green.beadalyo.kdh.menu.repository;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.model.GetAllMenuResInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    @Modifying
    @Query("update MenuEntity me set me.menuPic = :menuPic where me.menuPk = :menuPk")
    MenuEntity updateMenuPic(String menuPic, Long menuPk);

    @Query(value = "select   mn.menu_pk as menuPk, mn.menu_name as menuName, mn.menu_content as menuContent, " +
            "                mn.menu_price as menu_price, mn.menu_pic as menuPic, "+
           "                 mn.menu_state as menuState, mn.created_at as createdAt ,mn.updated_at as  updatedAt" +
             "      FROM menu mn" +
            "       WHERE mn.menu_Res_Pk = :menuResPk",nativeQuery = true)
    List<GetAllMenuResInterface> findAllByMenuResPk(Long menuResPk);


}
