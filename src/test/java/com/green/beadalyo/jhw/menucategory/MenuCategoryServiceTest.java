package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.menucategory.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuCategoryServiceTest {

    @InjectMocks
    private MenuCategoryService service;

    @Mock
    private MenuCategoryRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void insertMenuCat_ShouldInsertMenuCategory_WhenValidData() {
        MenuCatInsertDto dto = new MenuCatInsertDto();
        dto.setMenuCatName("새 카테고리");
        dto.setPosition(1L);
        dto.setRestaurant(new Restaurant());

        service.insertMenuCat(dto);

        MenuCategory expectedMenuCat = new MenuCategory(dto);

        verify(repository).save(argThat(menuCat ->
                menuCat.getMenuCategoryPk() == null &&
                        menuCat.getRestaurant().equals(expectedMenuCat.getRestaurant()) &&
                        menuCat.getMenuCatName().equals(expectedMenuCat.getMenuCatName()) &&
                        menuCat.getPosition().equals(expectedMenuCat.getPosition()) &&
                        menuCat.getCreatedAt() == null
        ));
    }

    @Test
    void getMenuCatCount_ShouldReturnCount_WhenValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        Long count = 5L;
        when(repository.countMenuCategories(restaurant)).thenReturn(count);

        Long result = service.getMenuCatCount(restaurant);

        assertEquals(count, result);
    }

    @Test
    void getMenuCatList_ShouldReturnList_WhenValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        List<MenuCategory> menuCategories = new ArrayList<>();
        when(repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant)).thenReturn(menuCategories);

        List<MenuCategory> result = service.getMenuCatList(restaurant);

        assertEquals(menuCategories, result);
    }

    @Test
    void getMenuCat_ShouldReturnMenuCategory_WhenValidId() {
        Long menuCatPk = 1L;
        MenuCategory menuCategory = new MenuCategory();
        when(repository.existsById(menuCatPk)).thenReturn(true);
        when(repository.getReferenceById(menuCatPk)).thenReturn(menuCategory);

        MenuCategory result = service.getMenuCat(menuCatPk);

        assertEquals(menuCategory, result);
    }

    @Test
    void getMenuCat_ShouldThrowException_WhenCategoryNotFound() {
        Long menuCatPk = 1L;
        when(repository.existsById(menuCatPk)).thenReturn(false);

        MenuCatNotFoundException thrown = assertThrows(MenuCatNotFoundException.class, () -> {
            service.getMenuCat(menuCatPk);
        });

        assertNotNull(thrown);
    }

    @Test
    @Transactional
    void deleteMenuCat_ShouldDeleteCategory_WhenValidId() {
        MenuCatInsertDto dto = new MenuCatInsertDto();
        dto.setMenuCatName("새 카테고리");
        dto.setPosition(1L);
        dto.setRestaurant(new Restaurant());

        service.insertMenuCat(dto);

        MenuCategory expectedMenuCat = new MenuCategory(dto);

        Long menuCatPk = 1L;
        Restaurant restaurant = new Restaurant();
        MenuCategory menuCategory = new MenuCategory(dto);
        List<MenuCategory> menuCategories = new ArrayList<>();
        menuCategories.add(menuCategory);

        when(repository.existsByMenuCategoryPkAndRestaurant(menuCatPk, restaurant)).thenReturn(true);
        when(repository.findByMenuCategoryPkAndRestaurant(menuCatPk, restaurant)).thenReturn(menuCategory);
        when(repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant)).thenReturn(menuCategories);


        int result = service.deleteMenuCat(menuCatPk, restaurant);

        assertEquals(1, result);
        verify(repository, times(1)).delete(argThat(menuCat ->
                menuCat.getMenuCategoryPk() == null &&
                        menuCat.getRestaurant().equals(expectedMenuCat.getRestaurant()) &&
                        menuCat.getMenuCatName().equals(expectedMenuCat.getMenuCatName()) &&
                        menuCat.getPosition().equals(expectedMenuCat.getPosition()) &&
                        menuCat.getCreatedAt() == null));
    }

    @Test
    @Transactional
    void patchMenuCat_ShouldUpdateCategory_WhenValidDto() {
        MenuCatPatchDto dto = new MenuCatPatchDto();
        dto.setMenuCatPk(1L);
        dto.setMenuCatName("수정된 카테고리");
        Restaurant restaurant = new Restaurant();
        dto.setRestaurant(restaurant);

        when(repository.existsByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), restaurant)).thenReturn(true);
        MenuCategory menuCategory = new MenuCategory();
        when(repository.findByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), restaurant)).thenReturn(menuCategory);

        int result = service.patchMenuCat(dto);

        assertEquals(1, result);
        assertEquals(dto.getMenuCatName(), menuCategory.getMenuCatName());
    }

    @Test
    @Transactional
    void patchMenuCatPosition_ShouldUpdateCategoryPosition_WhenValidDto() {
        MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto();
        dto.setMenuCatPk(1L);
        dto.setPosition(3L);
        Restaurant restaurant = new Restaurant();
        dto.setRestaurant(restaurant);

        when(repository.existsByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), restaurant)).thenReturn(true);
        MenuCategory menuCategory1 = new MenuCategory();
        menuCategory1.setPosition(1L);
        when(repository.getReferenceById(dto.getMenuCatPk())).thenReturn(menuCategory1);

        MenuCategory menuCategory2 = new MenuCategory();
        menuCategory2.setPosition(2L);

        MenuCategory menuCategory3 = new MenuCategory();
        menuCategory3.setPosition(3L);

        List<MenuCategory> categoriesBetween = new ArrayList<>();
        categoriesBetween.add(menuCategory1);
        categoriesBetween.add(menuCategory2);
        categoriesBetween.add(menuCategory3);
        System.out.println((categoriesBetween.get(categoriesBetween.size() - 1).getPosition()));
        when(repository.findMenuCategoriesByPositionBetweenAndRestaurant(restaurant, menuCategory1.getPosition(), dto.getPosition())).thenReturn(categoriesBetween);

        int result = service.patchMenuCatPosition(dto);

        assertEquals(1, result);
        assertEquals(dto.getPosition(), menuCategory1.getPosition());
    }
}
