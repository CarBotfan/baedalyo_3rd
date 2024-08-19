package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.common.FileUtils;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.RestaurantListView;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantDetailViewRepository;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantListViewRepository;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RestaurantServiceTest
{

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository repository;

    @Mock
    private RestaurantListViewRepository viewListRepository;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurantService.save(restaurant);
        verify(repository, times(1)).save(restaurant);
    }

    @Test
    void getRestaurantByCategory_shouldReturnRestaurants() throws Exception {
        BigDecimal x = BigDecimal.valueOf(1.0);
        BigDecimal y = BigDecimal.valueOf(1.0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RestaurantListView> expectedPage = new PageImpl<>(Collections.emptyList());

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(viewListRepository.findAllByCategoryIdAndCoordinates(any(), any(), any(), any(), any(), anyLong(), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<RestaurantListView> result = restaurantService.getRestaurantByCategory(0L, x, y, 1, 1, "");

        assertEquals(expectedPage, result);
    }

    @Test
    void getFollowRestaurantList_shouldReturnFollowedRestaurants() throws Exception {
        Page<RestaurantListView> expectedPage = new PageImpl<>(Collections.emptyList());
        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(viewListRepository.findFollowedRestaurant(anyLong(), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<RestaurantListView> result = restaurantService.getFollowRestaurantList(1, 1L);

        assertEquals(expectedPage, result);
    }

    @Test
    void getRestaurantData_shouldReturnRestaurant() throws Exception {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(repository.findTop1ByUser(user)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantData(user);

        assertEquals(restaurant, result);
    }

    @Test
    void getRestaurantData_shouldThrowDataNotFoundException() {
        User user = new User();
        when(repository.findTop1ByUser(user)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> restaurantService.getRestaurantData(user));
    }

    @Test
    void insertRestaurantData_shouldInsertRestaurant() throws Exception {
        RestaurantInsertDto dto = new RestaurantInsertDto();
        dto.setRegiNum("123-45-67890");
        dto.setUser(new User());

        restaurantService.insertRestaurantData(dto);

        verify(repository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void insertRestaurantData_shouldThrowDataWrongException() {
        RestaurantInsertDto dto = new RestaurantInsertDto();
        dto.setRegiNum("123456");

        assertThrows(DataWrongException.class, () -> restaurantService.insertRestaurantData(dto));
    }

    @Test
    void deleteRestaurantData_shouldMarkRestaurantAsClosed() throws Exception {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(repository.findTop1ByUser(user)).thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurantData(user);

        assertEquals(5, restaurant.getState());
        verify(repository, times(1)).save(restaurant);
    }

    @Test
    void toggleState_shouldToggleRestaurantState() throws Exception {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        restaurant.setState(1);
        when(repository.findTop1ByUser(user)).thenReturn(Optional.of(restaurant));

        Integer newState = restaurantService.toggleState(user);

        assertEquals(2, newState);
        verify(repository, times(1)).save(restaurant);
    }

    @Test
    void updateRestaurantPic_shouldUpdatePic() throws Exception {
        User user = new User();
        Restaurant restaurant = new Restaurant();
        restaurant.setPic("oldPic.jpg");
        restaurant.setSeq(1L);
        when(repository.findTop1ByUser(user)).thenReturn(Optional.of(restaurant));
        when(fileUtils.fileInput(anyString(), any(MultipartFile.class))).thenReturn("newPic.jpg");

        MultipartFile file = mock(MultipartFile.class);

        String newPic = restaurantService.updateRestaurantPic(user, file);

        assertEquals("newPic.jpg", newPic);
        verify(fileUtils, times(1)).fileDelete("oldPic.jpg");
        verify(fileUtils, times(1)).fileInput(anyString(), eq(file));
    }
}