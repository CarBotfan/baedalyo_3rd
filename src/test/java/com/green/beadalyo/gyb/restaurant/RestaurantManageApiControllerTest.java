package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.category.CategoryService;
import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.ResultError;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.request.RestaurantManagePatchReq;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantManageApiControllerTest
{

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private RestaurantManageApiController controller;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetManageData_Success() throws Exception
    {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        User user = new User();
        user.setUserPk(userPk);
        Restaurant restaurant = getRestaurant();
        when(userService.getUser(userPk)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);

        Result result = controller.getManageData();

        if (result instanceof ResultDto<?> resultDto) {
            assertEquals(1, resultDto.getStatusCode());
            // 추가적인 ResultDto에 대한 검증 로직
        } else if (result instanceof ResultError resultError) {
            // ResultError에 대한 검증 로직
            fail("Expected ResultDto but got ResultError: " + resultError.getResultMsg());
        } else {
            fail("Unexpected result type: " + result.getClass().getName());
        }
        verify(userService).getUser(userPk);
        verify(restaurantService).getRestaurantData(user);
    }



    @Test
    public void testGetManageData_UserNotFound() {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        when(userService.getUser(userPk)).thenThrow(new UserNotFoundException());

        Result result = controller.getManageData();

        assertEquals(-2, ((ResultError) result).getStatusCode());
        verify(userService).getUser(userPk);
    }

    @Test
    public void testToggleState_Success() throws Exception {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        User user = new User();
        when(userService.getUser(userPk)).thenReturn(user);
        when(restaurantService.toggleState(user)).thenReturn(1);

        Result result = controller.ToggleState();

        assertEquals(1, ((ResultDto<?>) result).getStatusCode());
        verify(restaurantService).toggleState(user);
    }

    @Test
    public void testToggleState_DataWrongException()throws Exception {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        User user = new User();
        when(userService.getUser(userPk)).thenReturn(user);
        when(restaurantService.toggleState(user)).thenThrow(new DataWrongException());

        Result result = controller.ToggleState();

        assertEquals(-4, ((ResultError) result).getStatusCode());
        verify(restaurantService).toggleState(user);
    }

    @Test
    public void testPatchManageData_Success() throws Exception {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(userService.getUser(userPk)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);

        RestaurantManagePatchReq req = new RestaurantManagePatchReq();
        req.setRegiNum("123-45-67890");
        req.setRestaurantName("Test Restaurant");

        Result result = controller.patchManageData(req);

        assertEquals(1, ((ResultDto<?>) result).getStatusCode());
        verify(restaurantService).save(restaurant);
    }

    @Test
    public void testPatchManageData_InvalidRegiNum() throws Exception {
        RestaurantManagePatchReq req = new RestaurantManagePatchReq();
        req.setRegiNum("invalid");

        Result result = controller.patchManageData(req);

        assertEquals(-4, ((ResultError) result).getStatusCode());
    }

    @Test
    public void testUpdatePic_Success() throws Exception {
        long userPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        User user = new User();
        when(userService.getUser(userPk)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(new Restaurant());

        MultipartFile file = mock(MultipartFile.class);
        when(restaurantService.updateRestaurantPic(user, file)).thenReturn("newPic.jpg");

        Result result = controller.updatePic(file);

        assertEquals(1, ((ResultDto<?>) result).getStatusCode());
        verify(restaurantService).save(any(Restaurant.class));
    }


    private static Restaurant getRestaurant()
    {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street, Test City");
        restaurant.setCoorX(37.5665);
        restaurant.setCoorY(126.9780);
        restaurant.setRegiNum("123-45-67891");
        restaurant.setRestaurantDescription("This is a test restaurant.");
        restaurant.setReviewDescription("This restaurant has excellent reviews.");
        restaurant.setState(1); // 예: 1 = 영업중, 2 = 휴업중
        restaurant.setOpenTime(LocalTime.of(11, 0)); // 오전 11시 개점
        restaurant.setCloseTime(LocalTime.of(22, 0)); // 오후 10시 폐점
        restaurant.setPic("/images/test-restaurant.jpg");
        return restaurant;
    }
}

