package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.gyb.category.CategoryService;
import com.green.beadalyo.gyb.common.*;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.RestaurantDetailView;
import com.green.beadalyo.gyb.model.RestaurantListView;
import com.green.beadalyo.gyb.response.CategoryRes;
import com.green.beadalyo.gyb.response.RestaurantDetailRes;
import com.green.beadalyo.gyb.response.RestaurantListRes;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import com.green.beadalyo.jhw.useraddr.UserAddrServiceImpl;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import com.green.beadalyo.kdh.menu.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantApiController.class)
class RestaurantApiControllerTest
{
    @InjectMocks
    private RestaurantApiController restaurantApiController;

    @MockBean
    private RestaurantService restaurantService;  // MockBean으로 등록

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private MenuService menuService;

    @MockBean
    private UserAddrServiceImpl userAddrService;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // JwtTokenProvider를 MockBean으로 등록

    @MockBean
    private AppProperties appProperties;  // AppProperties를 MockBean으로 등록

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RestaurantApiController(restaurantService, categoryService, menuService, userAddrService, authenticationFacade)).build();
    }

    @Test
    void getRestaurantList_shouldReturnRestaurantList() throws Exception {
        List<RestaurantListView> restaurants = new ArrayList<>();
        restaurants.add(new RestaurantListView(
                1L,
                "Restaurant A",
                4.5f,
                120,
                "123 Street, City",
                1,
                "picA.jpg",
                new BigDecimal("37.7749"),
                new BigDecimal("-122.4194"),
                LocalDateTime.now().minusDays(5),
                1,1,1
        ));

        restaurants.add(new RestaurantListView(
                2L,
                "Restaurant B",
                4.0f,
                85,
                "456 Avenue, City",
                1,
                "picB.jpg",
                new BigDecimal("34.0522"),
                new BigDecimal("-118.2437"),
                LocalDateTime.now().minusDays(10),
                0,1,1
        ));

        restaurants.add(new RestaurantListView(
                3L,
                "Restaurant C",
                3.8f,
                230,
                "789 Boulevard, City",
                0,
                "picC.jpg",
                new BigDecimal("40.7128"),
                new BigDecimal("-74.0060"),
                LocalDateTime.now().minusDays(2),
                1,1,1
        ));
        Page<RestaurantListView> pageList = new PageImpl<>(restaurants); // PageImpl에 실제 데이터를 넣어줍니다.

        when(restaurantService.getRestaurantByCategory(
                any(Long.class), any(BigDecimal.class), any(BigDecimal.class),
                any(Integer.class), any(Integer.class), any(String.class)))
                .thenReturn(pageList);

        ResultActions resultActions = mockMvc.perform(get("/api/restaurant")
                        .param("category_id", "0")
                        .param("page", "1")
                        .param("order_type", "1")
                        .param("addrX", "37.5665")
                        .param("addrY", "126.9780")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultData").exists());

        MvcResult result = resultActions.andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("Response Content: " + content);
    }

    @Test
    void getRestaurant_shouldReturnRestaurantDetails() throws Exception {
        RestaurantDetailView detailView = new RestaurantDetailView();
        when(restaurantService.getRestaurantDataViewBySeq(any(Long.class))).thenReturn(detailView);
        when(menuService.getMenuList(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultData").exists());
    }

    @Test
    void getCategoryList_shouldReturnCategoryList() throws Exception {
        List<Category> categories = Collections.emptyList();
        when(categoryService.getCategoryAll()).thenReturn(categories);

        mockMvc.perform(get("/api/restaurant/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultData").exists());
    }

    @Test
    void getFollowList_shouldReturnFollowedRestaurants() throws Exception {
        Page<RestaurantListView> pageList = new PageImpl<>(Collections.emptyList());
        when(restaurantService.getFollowRestaurantList(any(Integer.class),any())).thenReturn(pageList);

        mockMvc.perform(get("/api/restaurant/followed")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultData").exists());
    }
}