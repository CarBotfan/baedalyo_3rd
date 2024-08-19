package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.menucategory.model.*;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuCategoryControllerTest {

    @InjectMocks
    private MenuCategoryController controller;

    @Mock
    private MenuCategoryService service;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void postMenuCat_ShouldReturnSuccess_WhenValidData() throws Exception{
        MenuCatPostReq request = new MenuCatPostReq();
        request.setMenuCategoryName("새 카테고리");

        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);
        when(service.getMenuCatCount(restaurant)).thenReturn(0L);

        ResultDto<Integer> response = controller.postMenuCat(request);

        verify(service, times(1)).insertMenuCat(any(MenuCatInsertDto.class));
        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 등록 완료", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void postMenuCat_ShouldReturnError_WhenDataNotFoundException() throws Exception {
        MenuCatPostReq request = new MenuCatPostReq();
        request.setMenuCategoryName("새 카테고리");

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(restaurantService.getRestaurantData(new User())).thenThrow(new DataNotFoundException("식당 정보를 찾을 수 없음"));

        ResultDto<Integer> response = controller.postMenuCat(request);

        assertEquals(-2, response.getStatusCode());
        assertEquals("식당 정보를 찾을 수 없음", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getMenuCatResList_ShouldReturnList_WhenValidData() throws Exception {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        List<MenuCategory> menuCategories = new ArrayList<>();
        menuCategories.add(new MenuCategory());

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);
        when(service.getMenuCatList(restaurant)).thenReturn(menuCategories);

        ResultDto<List<MenuCatGetRes>> response = controller.getMenuCatResList();

        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 목록 조회 성공", response.getResultMsg());
        assertNotNull(response.getResultData());
        assertEquals(menuCategories.size(), response.getResultData().size());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getMenuCatResList_ShouldReturnError_WhenMenuCatNotFoundException() throws Exception {
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(restaurantService.getRestaurantData(any(User.class))).thenThrow(new DataNotFoundException("식당 정보를 찾을 수 없음"));

        ResultDto<List<MenuCatGetRes>> response = controller.getMenuCatResList();

        assertEquals(-2, response.getStatusCode());
        assertEquals("식당 정보를 찾을 수 없음", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getMenuCat_ShouldReturnMenuCat_WhenValidId() {
        Long menuCatPk = 1L;
        MenuCategory menuCategory = new MenuCategory();
        MenuCatGetRes expectedResponse = new MenuCatGetRes(menuCategory);

        when(service.getMenuCat(menuCatPk)).thenReturn(menuCategory);

        ResultDto<MenuCatGetRes> response = controller.getMenuCat(menuCatPk);

        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 조회 완료", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getMenuCat_ShouldReturnError_WhenMenuCatNotFoundException() {
        Long menuCatPk = 1L;

        when(service.getMenuCat(menuCatPk)).thenThrow(new MenuCatNotFoundException());

        ResultDto<MenuCatGetRes> response = controller.getMenuCat(menuCatPk);

        assertEquals(-3, response.getStatusCode());
        assertEquals("메뉴 카테고리 정보 조회에 실패했습니다.", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void deleteMenuCat_ShouldReturnSuccess_WhenValidId() throws Exception {
        Long menuCatPk = 1L;
        Restaurant restaurant = new Restaurant();
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(restaurantService.getRestaurantData(any(User.class))).thenReturn(restaurant);
        when(service.deleteMenuCat(menuCatPk, restaurant)).thenReturn(1);

        ResultDto<Integer> response = controller.deleteMenuCat(menuCatPk);

        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 삭제 완료", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void deleteMenuCat_ShouldReturnError_WhenMenuCatNotFoundException() throws Exception {
        Long menuCatPk = 1L;
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(restaurantService.getRestaurantData(any(User.class))).thenReturn(new Restaurant());
        when(service.deleteMenuCat(menuCatPk, any(Restaurant.class))).thenThrow(new MenuCatNotFoundException());

        ResultDto<Integer> response = controller.deleteMenuCat(menuCatPk);

        assertEquals(-3, response.getStatusCode());
        assertEquals("메뉴 카테고리 정보 조회에 실패했습니다.", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void patchMenuCat_ShouldReturnSuccess_WhenValidDto() throws Exception {
        MenuCatPatchReq request = new MenuCatPatchReq();
        request.setMenuCatPk(1L);
        request.setMenuCatName("수정된 카테고리");

        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);
        when(service.patchMenuCat(any(MenuCatPatchDto.class))).thenReturn(1);

        ResultDto<Integer> response = controller.patchMenuCat(request);

        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 수정 완료", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void patchMenuCat_ShouldReturnError_WhenMenuCatNotFoundException() throws Exception {
        MenuCatPatchReq request = new MenuCatPatchReq();
        request.setMenuCatPk(1L);

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(restaurantService.getRestaurantData(any(User.class))).thenReturn(new Restaurant());
        when(service.patchMenuCat(any(MenuCatPatchDto.class))).thenThrow(new MenuCatNotFoundException());

        ResultDto<Integer> response = controller.patchMenuCat(request);

        assertEquals(-3, response.getStatusCode());
        assertEquals("메뉴 카테고리 정보 조회에 실패했습니다.", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void patchMenuPosition_ShouldReturnSuccess_WhenValidDto() throws Exception {
        MenuCatPositionPatchReq request = new MenuCatPositionPatchReq();
        request.setMenuCatPk(1L);
        request.setPosition(2L);

        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(user);
        when(restaurantService.getRestaurantData(user)).thenReturn(restaurant);
        when(service.patchMenuCatPosition(any(MenuCatPositionPatchDto.class))).thenReturn(1);

        ResultDto<Integer> response = controller.patchMenuPosition(request);

        assertEquals(1, response.getStatusCode());
        assertEquals("카테고리 수정 완료", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void patchMenuPosition_ShouldReturnError_WhenInvalidPosition() {
        MenuCatPositionPatchReq request = new MenuCatPositionPatchReq();
        request.setMenuCatPk(1L);
        request.setPosition(-1L);

        ResultDto<Integer> response = controller.patchMenuPosition(request);

        assertEquals(-1, response.getStatusCode());
        assertEquals("올바르지 않은 위치입니다.", response.getResultMsg());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void patchMenuPosition_ShouldReturnError_WhenMenuCatNotFoundException() throws Exception {
        MenuCatPositionPatchReq request = new MenuCatPositionPatchReq();
        request.setMenuCatPk(1L);
        request.setPosition(2L);

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(restaurantService.getRestaurantData(any(User.class))).thenReturn(new Restaurant());
        when(service.patchMenuCatPosition(any(MenuCatPositionPatchDto.class))).thenThrow(new MenuCatNotFoundException());

        ResultDto<Integer> response = controller.patchMenuPosition(request);

        assertEquals(-3, response.getStatusCode());
        assertEquals("메뉴 카테고리 정보 조회에 실패했습니다.", response.getResultMsg());
    }
}