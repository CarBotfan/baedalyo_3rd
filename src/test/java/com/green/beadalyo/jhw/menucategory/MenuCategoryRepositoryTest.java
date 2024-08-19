package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.jhw.menucategory.MenuCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class MenuCategoryRepositoryTest {

    @Autowired
    private MenuCategoryRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // 데이터베이스에 사용할 Restaurant 객체를 생성 및 저장
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurantRepository.save(restaurant);

        // 초기 데이터 세팅
        for (int i = 1; i <= 5; i++) {
            MenuCategory menuCategory = new MenuCategory();
            menuCategory.setMenuCatName("Category " + i);
            menuCategory.setPosition((long) i);
            menuCategory.setRestaurant(restaurant);
            repository.save(menuCategory);
        }
    }

    @Test
    void countMenuCategories_ShouldReturnCorrectCount() {
        Long count = repository.countMenuCategories(restaurant);
        assertEquals(5, count);
    }

    @Test
    void findByMenuCategoryPkAndRestaurant_ShouldReturnMenuCategory_WhenExists() {
        MenuCategory menuCategory = repository.findByMenuCategoryPkAndRestaurant(1L, restaurant);
        assertNotNull(menuCategory);
        assertEquals("Category 1", menuCategory.getMenuCatName());
    }

    @Test
    void findMenuCategoriesByRestaurantOrderByPosition_ShouldReturnCategoriesInOrder() {
        List<MenuCategory> categories = repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant);
        assertNotNull(categories);
        assertEquals(5, categories.size());
        assertEquals("Category 1", categories.get(0).getMenuCatName());
        assertEquals("Category 5", categories.get(4).getMenuCatName());
    }

    @Test
    void existsByMenuCategoryPkAndRestaurant_ShouldReturnTrue_WhenExists() {
        Boolean exists = repository.existsByMenuCategoryPkAndRestaurant(1L, restaurant);
        assertTrue(exists);
    }

    @Test
    void existsByMenuCategoryPkAndRestaurant_ShouldReturnFalse_WhenNotExists() {
        Boolean exists = repository.existsByMenuCategoryPkAndRestaurant(100L, restaurant);
        assertFalse(exists);
    }

    @Test
    void findMenuCategoriesByPositionBetweenAndRestaurant_ShouldReturnCategoriesInPositionRange() {
        List<MenuCategory> categories = repository.findMenuCategoriesByPositionBetweenAndRestaurant(
                restaurant, 2L, 4L);
        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertEquals("Category 2", categories.get(0).getMenuCatName());
        assertEquals("Category 4", categories.get(2).getMenuCatName());
    }
}