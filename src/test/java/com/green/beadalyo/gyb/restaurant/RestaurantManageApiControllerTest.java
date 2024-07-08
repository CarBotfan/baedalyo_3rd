package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.category.CategoryService;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RestaurantManageApiController.class)
class RestaurantManageApiControllerTest
{

    @MockBean private RestaurantService service ;
    @MockBean private CategoryService categoryService ;

    @Autowired private MockMvc mvc ;
    @Test
    void getManageData()
    {
//        given()
//
//        mvc.perform(
//                get("api/restaurant/manage")
//        ).andExpect(status().isOk())
//                .andExpect(content().json())
    }

    @Test
    void toggleState()
    {
    }

    @Test
    void patchManageData()
    {
    }

    @Test
    void updatePic()
    {
    }

    @Test
    void putRestaurantCategory()
    {
    }

    @Test
    void deleteRestaurantCategory()
    {
    }
}