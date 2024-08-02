package com.green.beadalyo.jhw.MenuCategory;


import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    @Query("SELECT COUNT(mc.menuCategoryPk) FROM MenuCategory mc WHERE mc.restaurant = :restaurant")
    Long countMenuCategories(Restaurant restaurant);
}
