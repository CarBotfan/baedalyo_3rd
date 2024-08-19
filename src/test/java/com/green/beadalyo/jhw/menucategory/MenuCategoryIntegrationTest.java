package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.jhw.menucategory.model.MenuCatInsertDto;
import com.green.beadalyo.jhw.menucategory.MenuCategoryRepository;
import com.green.beadalyo.gyb.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 실제 데이터베이스 사용
@ActiveProfiles("tdd")  // 테스트 프로파일 사용
public class MenuCategoryIntegrationTest {

    @Autowired
    private MenuCategoryRepository repository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        // 필요한 경우 Restaurant 객체를 데이터베이스에 저장
    }

    @Test
    void insertMenuCategory_ShouldSaveMenuCategory_WhenValidData() {
        MenuCatInsertDto dto = new MenuCatInsertDto();
        dto.setMenuCatName("새 카테고리");
        dto.setPosition(1L);
        dto.setRestaurant(restaurant);

        MenuCategory menuCategory = new MenuCategory(dto);
        repository.save(menuCategory);

        Optional<MenuCategory> savedMenuCategory = repository.findById(menuCategory.getMenuCategoryPk());
        assertTrue(savedMenuCategory.isPresent(), "MenuCategory should be saved");
        assertEquals("새 카테고리", savedMenuCategory.get().getMenuCatName());
        assertEquals(1L, savedMenuCategory.get().getPosition());
    }

    @Test
    void updateMenuCategory_ShouldUpdateMenuCategory_WhenValidData() {
        MenuCatInsertDto dto = new MenuCatInsertDto();
        dto.setMenuCatName("새 카테고리");
        dto.setPosition(1L);
        dto.setRestaurant(restaurant);

        MenuCategory menuCategory = new MenuCategory(dto);
        repository.save(menuCategory);

        // Update menuCategory
        menuCategory.setMenuCatName("업데이트된 카테고리");
        repository.save(menuCategory);

        Optional<MenuCategory> updatedMenuCategory = repository.findById(menuCategory.getMenuCategoryPk());
        assertTrue(updatedMenuCategory.isPresent(), "MenuCategory should be updated");
        assertEquals("업데이트된 카테고리", updatedMenuCategory.get().getMenuCatName());
    }

    @Test
    void deleteMenuCategory_ShouldDeleteMenuCategory_WhenExists() {
        MenuCatInsertDto dto = new MenuCatInsertDto();
        dto.setMenuCatName("삭제할 카테고리");
        dto.setPosition(1L);
        dto.setRestaurant(restaurant);

        MenuCategory menuCategory = new MenuCategory(dto);
        repository.save(menuCategory);

        repository.delete(menuCategory);

        Optional<MenuCategory> deletedMenuCategory = repository.findById(menuCategory.getMenuCategoryPk());
        assertFalse(deletedMenuCategory.isPresent(), "MenuCategory should be deleted");
    }
}