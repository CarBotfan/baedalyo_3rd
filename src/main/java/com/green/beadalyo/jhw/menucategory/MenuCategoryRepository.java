package com.green.beadalyo.jhw.menucategory;


import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    @Query("SELECT COUNT(mc.menuCategoryPk) FROM MenuCategory mc WHERE mc.restaurant = :restaurant")
    Long countMenuCategories(Restaurant restaurant);


    MenuCategory findByMenuCategoryPkAndRestaurant(Long menuCatPk, Restaurant restaurant);

    List<MenuCategory> findMenuCategoriesByRestaurantOrderByPosition(Restaurant restaurant);
    Boolean existsByMenuCategoryPkAndRestaurant(Long menuCatPk, Restaurant restaurant);

    @Query("SELECT mc from MenuCategory mc WHERE mc.restaurant = :restaurant AND mc.position between :fromPosition AND :toPosition ORDER BY mc.position ")
    List<MenuCategory> findMenuCategoriesByPositionBetweenAndRestuaurant(Restaurant restaurant, Long fromPosition, Long toPosition);
}
