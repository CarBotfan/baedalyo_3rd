package com.green.beadalyo.jhw.menucategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.model.MenuCatDto;
import com.green.beadalyo.jhw.menucategory.model.MenuCatInsertDto;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

        System.out.println(menuCategory);
    }

    @Test
    void patchMenuCat() {
        Restaurant res = new Restaurant();
        given(repository.existsByMenuCategoryPkAndRestaurant(1L, res));
    }

}
