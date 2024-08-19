package com.green.beadalyo.lmy;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.resfollow.ResFollowService;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import com.green.beadalyo.lmy.resfollow.repository.ResFollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResFollowServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ResFollowRepository resFollowRepository;

    @InjectMocks
    private ResFollowService resFollowService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRes() {
        Long seq = 1L;
        Restaurant mockRestaurant = new Restaurant();
        when(restaurantRepository.findRestaurantBySeq(seq)).thenReturn(mockRestaurant);

        Restaurant result = resFollowService.getRes(seq);

        assertNotNull(result);
        verify(restaurantRepository, times(1)).findRestaurantBySeq(seq);
    }

    @Test
    void testGetResFollow() {
        Restaurant mockRestaurant = new Restaurant();
        User mockUser = new User();
        ResFollow mockResFollow = new ResFollow();

        when(resFollowRepository.findResFollowByResPkAndUserPk(mockRestaurant, mockUser)).thenReturn(mockResFollow);

        ResFollow result = resFollowService.getResFollow(mockRestaurant, mockUser);

        assertNotNull(result);
        verify(resFollowRepository, times(1)).findResFollowByResPkAndUserPk(mockRestaurant, mockUser);
    }

    @Test
    void testSaveResFollow() {
        Restaurant mockRestaurant = new Restaurant();
        User mockUser = new User();
        ResFollow savedResFollow = new ResFollow();
        savedResFollow.setResPk(mockRestaurant);
        savedResFollow.setUserPk(mockUser);

        when(resFollowRepository.save(any(ResFollow.class))).thenReturn(savedResFollow);

        Integer result = resFollowService.saveResFollow(mockRestaurant, mockUser);

        assertEquals(1, result);
        verify(resFollowRepository, times(1)).save(any(ResFollow.class));
    }

    @Test
    void testDeleteResFollow() {
        ResFollow mockResFollow = new ResFollow();

        doNothing().when(resFollowRepository).delete(mockResFollow);

        Integer result = resFollowService.deleteResFollow(mockResFollow);

        assertEquals(0, result);
        verify(resFollowRepository, times(1)).delete(mockResFollow);
    }

    @Test
    void testGetResNotFound() {
        Long seq = 1L;
        when(restaurantRepository.findRestaurantBySeq(seq)).thenReturn(null);

        Restaurant result = resFollowService.getRes(seq);

        assertNull(result);
        verify(restaurantRepository, times(1)).findRestaurantBySeq(seq);
    }

    @Test
    void testGetResFollowNotFound() {
        Restaurant mockRestaurant = new Restaurant();
        User mockUser = new User();

        when(resFollowRepository.findResFollowByResPkAndUserPk(mockRestaurant, mockUser)).thenReturn(null);

        ResFollow result = resFollowService.getResFollow(mockRestaurant, mockUser);

        assertNull(result);
        verify(resFollowRepository, times(1)).findResFollowByResPkAndUserPk(mockRestaurant, mockUser);
    }
}
