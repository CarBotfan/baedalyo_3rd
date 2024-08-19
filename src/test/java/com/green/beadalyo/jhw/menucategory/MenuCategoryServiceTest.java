package com.green.beadalyo.jhw.menucategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Import(MenuCategoryService.class)
@ExtendWith(SpringExtension.class)
public class MenuCategoryServiceTest {
    @MockBean private MenuCategoryRepository repository;

    @Autowired
    private MenuCategoryService service;

    @Test
    void getMenuCatCount() {
        Restaurant restaurant = new Restaurant();
        restaurant.setSeq(1L);
        MenuCategory menuCat1 = new MenuCategory();
        MenuCategory menuCat2 = new MenuCategory();
        MenuCategory menuCat3 = new MenuCategory();
        given(repository.countMenuCategories(restaurant)).willReturn((long) List.of(menuCat1, menuCat2, menuCat3).size());
        Long count = service.getMenuCatCount(restaurant);
        System.out.println(count);
    }

    @Test
    void GetMenuCat() {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setMenuCategoryPk(1L);
        menuCategory.setMenuCatName("Test1");
        menuCategory.setPosition(1L);
        given(repository.existsById(1L)).willReturn(true);
        given(repository.getReferenceById(1L)).willReturn(menuCategory);

        System.out.println(menuCategory.getMenuCatName());
    }

    @Test
    void deleteMenuCat() {
        Restaurant restaurant = new Restaurant();
        MenuCategory menuCat1 = new MenuCategory();
        menuCat1.setPosition(1L);
        menuCat1.setMenuCategoryPk(1L);
        MenuCategory menuCat2 = new MenuCategory();
        menuCat2.setPosition(2L);
        menuCat2.setMenuCategoryPk(2L);
        MenuCategory menuCat3 = new MenuCategory();
        menuCat3.setPosition(3L);
        menuCat3.setMenuCategoryPk(3L);
        given(repository.existsByMenuCategoryPkAndRestaurant(2L, restaurant)).willReturn(true);
        given(repository.findByMenuCategoryPkAndRestaurant(2L, restaurant)).willReturn(menuCat2);
        given(repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant)).willReturn(List.of(menuCat1, menuCat2, menuCat3));
        service.deleteMenuCat(2L, restaurant);

        System.out.println(List.of(menuCat1, menuCat3));
    }

    @Test
    void patchMenuCat() {
        Restaurant restaurant = new Restaurant();
        MenuCategory menuCat1 = new MenuCategory();
        menuCat1.setMenuCatName("Test");
        menuCat1.setMenuCategoryPk(1L);

        given(repository.existsByMenuCategoryPkAndRestaurant(1L, restaurant)).willReturn(true);
        given(repository.findByMenuCategoryPkAndRestaurant(1L, restaurant)).willReturn(menuCat1);

        MenuCatPatchDto dto = new MenuCatPatchDto();
        dto.setMenuCatPk(1L);
        dto.setMenuCatName("TestSuccess");
        dto.setRestaurant(restaurant);

        service.patchMenuCat(dto);

        System.out.println(menuCat1.getMenuCatName());
    }

    @Test
    void patchMenuCatPosition() {
        Restaurant restaurant = new Restaurant();

        MenuCatPositionPatchDto dto1 = new MenuCatPositionPatchDto();
        dto1.setMenuCatPk(1L);
        dto1.setRestaurant(restaurant);
        dto1.setPosition(3L);

        MenuCatPositionPatchDto dto2 = new MenuCatPositionPatchDto();
        dto2.setMenuCatPk(3L);
        dto2.setRestaurant(restaurant);
        dto2.setPosition(1L);

        MenuCategory menuCat1 = new MenuCategory();
        menuCat1.setPosition(1L);
        menuCat1.setMenuCategoryPk(1L);
        MenuCategory menuCat2 = new MenuCategory();
        menuCat2.setPosition(2L);
        menuCat2.setMenuCategoryPk(2L);
        MenuCategory menuCat3 = new MenuCategory();
        menuCat3.setPosition(3L);
        menuCat3.setMenuCategoryPk(3L);

        List<MenuCategory> list = List.of(menuCat1, menuCat2, menuCat3);

        given(repository.existsByMenuCategoryPkAndRestaurant(1L, restaurant)).willReturn(true);
        given(repository.existsByMenuCategoryPkAndRestaurant(3L, restaurant)).willReturn(true);
        given(repository.getReferenceById(1L)).willReturn(menuCat1);
        given(repository.findMenuCategoriesByPositionBetweenAndRestaurant(restaurant, 1L, 3L)).willReturn(List.of(menuCat1, menuCat2, menuCat3));

        service.patchMenuCatPosition(dto1);

        System.out.println(list);


    }
}
