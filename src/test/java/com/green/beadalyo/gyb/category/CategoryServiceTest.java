package com.green.beadalyo.gyb.category;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(CategoryService.class)
@ExtendWith(SpringExtension.class)
class CategoryServiceTest
{

    @MockBean private CategoryRepository repository ;

    @MockBean private MatchingCateforyRepository matchingRepository ;

    @Autowired private CategoryService service;

    @Test
    void getCategoryAll()
    {
        Category category1 = new Category();
        category1.setCategoryName("한식");
        category1.setRestaurants(List.of(new Restaurant()));
        Category category2 = new Category();
        category2.setCategoryName("한식");
        category2.setRestaurants(List.of(new Restaurant()));
        given(repository.findAll()).willReturn(List.of(category1, category2));
        List<Category> categories = service.getCategoryAll();
        System.out.println(categories);
    }

    @Test
    void getCategory()
    {
        Category category1 = new Category();
        category1.setCategoryName("한식");
        category1.setRestaurants(List.of(new Restaurant()));
        given(repository.findById(1L)).willReturn(Optional.of(category1)) ;
        Category cate = service.getCategory(1L) ;
        assertEquals(cate, category1);
    }

    @Test
    void insertCategory()
    {
    }

    @Test
    void deleteCategory()
    {
    }

    @Test
    void insertRestaurantCategory()
    {
    }

    @Test
    void deleteRestaurantCategory()
    {
    }
}