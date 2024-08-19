package com.green.beadalyo.lmy;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.resfollow.ResFollowController;
import com.green.beadalyo.lmy.resfollow.ResFollowService;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResFollowControllerTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ResFollowService resFollowService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private ResFollowController resFollowController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(resFollowController).build();
    }

    @Test
    void testToggleFollow_AddFollow() throws Exception {
        Long resPk = 1L;
        User mockUser = new User();
        Restaurant mockRestaurant = new Restaurant();

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(mockUser);
        when(resFollowService.getRes(resPk)).thenReturn(mockRestaurant);
        when(resFollowService.getResFollow(mockRestaurant, mockUser)).thenReturn(null);
        when(resFollowService.saveResFollow(mockRestaurant, mockUser)).thenReturn(1);

        mockMvc.perform(put("/api/follow/toggle/{res_pk}", resPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(1))
                .andExpect(jsonPath("$.resultMsg").value("좋아요 완료"))
                .andExpect(jsonPath("$.resultData").value(1));

        verify(resFollowService, times(1)).saveResFollow(mockRestaurant, mockUser);
    }

    @Test
    void testToggleFollow_RemoveFollow() throws Exception {
        Long resPk = 1L;
        User mockUser = new User();
        Restaurant mockRestaurant = new Restaurant();
        ResFollow mockResFollow = new ResFollow();

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(mockUser);
        when(resFollowService.getRes(resPk)).thenReturn(mockRestaurant);
        when(resFollowService.getResFollow(mockRestaurant, mockUser)).thenReturn(mockResFollow);
        when(resFollowService.deleteResFollow(mockResFollow)).thenReturn(0);

        mockMvc.perform(put("/api/follow/toggle/{res_pk}", resPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(1))
                .andExpect(jsonPath("$.resultMsg").value("좋아요 취소"))
                .andExpect(jsonPath("$.resultData").value(0));

        verify(resFollowService, times(1)).deleteResFollow(mockResFollow);
    }

    @Test
    void testToggleFollow_RestaurantNotFound() throws Exception {
        Long resPk = 1L;

        when(authenticationFacade.getLoginUserPk()).thenReturn(1L);
        when(userService.getUser(1L)).thenReturn(new User());
        when(resFollowService.getRes(resPk)).thenReturn(null);

        mockMvc.perform(put("/api/follow/toggle/{res_pk}", resPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(-2))
                .andExpect(jsonPath("$.resultMsg").value("존재하지 않는 상점입니다."));

        verify(resFollowService, never()).saveResFollow(any(Restaurant.class), any(User.class));
        verify(resFollowService, never()).deleteResFollow(any(ResFollow.class));
    }
}
