package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.menucategory.model.MenuCatPositionPatchDto;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.jhw.menucategory.model.MenuCatInsertDto;
import com.green.beadalyo.jhw.menucategory.MenuCategoryRepository;
import com.green.beadalyo.gyb.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 실제 데이터베이스 사용
@ActiveProfiles("tdd")  // 테스트 프로파일 사용
@Import(MenuCategoryService.class)
public class MenuCategoryIntegrationTest {


    @Autowired
    private MenuCategoryRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuCategoryService service;

    private Restaurant restaurant;

    private MenuCategory menuCategory1;
    private MenuCategory menuCategory2;
    private MenuCategory menuCategory3;
    private MenuCategory menuCategory4;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("address");
        restaurant.setRegiNum("333-44-55555");
        // 필요한 경우 Restaurant 객체를 데이터베이스에 저장

        restaurantRepository.save(restaurant);

        menuCategory1 = new MenuCategory();
        menuCategory1.setMenuCatName("TestCategory1");
        menuCategory1.setPosition(1L);

        menuCategory2 = new MenuCategory();
        menuCategory2.setMenuCatName("TestCategory2");
        menuCategory2.setPosition(2L);

        menuCategory3 = new MenuCategory();
        menuCategory3.setMenuCatName("TestCategory3");
        menuCategory3.setPosition(3L);

        menuCategory4 = new MenuCategory();
        menuCategory4.setMenuCatName("TestCategory4");
        menuCategory4.setPosition(4L);

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
    @Test
    void patchMenuCatPosition_ShouldUpdatePosition_WhenPositionIsIncreased() {
        // Given
        MenuCategory cat1 = createAndSaveMenuCategory(restaurant, "Category 1", 1L);
        MenuCategory cat2 = createAndSaveMenuCategory(restaurant, "Category 2", 2L);

        MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto(cat1.getMenuCategoryPk(), 2L, restaurant);

        // When
        int result = service.patchMenuCatPosition(dto);

        // Then
        assertEquals(1, result);
        assertEquals(2L, repository.findById(cat1.getMenuCategoryPk()).get().getPosition());
        assertEquals(1L, repository.findById(cat2.getMenuCategoryPk()).get().getPosition());
    }

    @Test
    void patchMenuCatPosition_ShouldUpdatePosition_WhenPositionIsDecreased() {
        // Given
        MenuCategory cat1 = createAndSaveMenuCategory(restaurant, "Category 1", 1L);
        MenuCategory cat2 = createAndSaveMenuCategory(restaurant, "Category 2", 2L);

        MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto(cat2.getMenuCategoryPk(), 1L, restaurant);

        // When
        int result = service.patchMenuCatPosition(dto);

        // Then
        assertEquals(1, result);
        assertEquals(1L, repository.findById(cat2.getMenuCategoryPk()).get().getPosition());
        assertEquals(2L, repository.findById(cat1.getMenuCategoryPk()).get().getPosition());
    }

    @Test
    void patchMenuCatPosition_ShouldThrowException_WhenMenuCategoryNotFound() {
        // Given
        MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto(999L, 1L, restaurant);

        // When & Then
        assertThrows(MenuCatNotFoundException.class, () -> {
            service.patchMenuCatPosition(dto);
        });
    }

    @Test
    void patchMenuCatPosition_ShouldThrowException_WhenInvalidPosition() {

        // Given
        MenuCategory cat1 = createAndSaveMenuCategory(restaurant, "Category 1", 1L);

        MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto(cat1.getMenuCategoryPk(), 0L, restaurant);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            service.patchMenuCatPosition(dto);
        });
    }
    private MenuCategory createAndSaveMenuCategory(Restaurant restaurant, String name, Long position) {
        MenuCategory category = new MenuCategory();
        category.setRestaurant(restaurant);
        category.setMenuCatName(name);
        category.setPosition(position);
        return repository.save(category);
    }
}
