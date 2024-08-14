package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.email.MailService;
import com.green.beadalyo.jhw.menucategory.model.MenuCatInsertDto;
import com.green.beadalyo.jhw.menucategory.model.MenuCatPostReq;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserSignUpPostReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

@Import(MenuCategoryController.class)
@ExtendWith(SpringExtension.class)
public class MenuCategoryControllerTest {
    @MockBean private MenuCategoryService service;
    @MockBean private RestaurantService restaurantService;
    @MockBean private UserServiceImpl userService;

    @MockBean private AuthenticationFacade authenticationFacade;

    @Autowired private MenuCategoryController controller;

    @Test
    void postMenuCat() {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        given(authenticationFacade.getLoginUserPk()).willReturn(1L);
        given(userService.getUser(1L)).willReturn(user);
        try {
            given(restaurantService.getRestaurantData(user)).willReturn(restaurant);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        given(service.getMenuCatCount(restaurant)).willReturn(3L);
        MenuCatPostReq req = new MenuCatPostReq();

    }

}
