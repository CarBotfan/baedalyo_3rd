package com.green.beadalyo.lmy;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import com.green.beadalyo.lmy.resfollow.repository.ResFollowRepository;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ResFollowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ResFollowRepository resFollowRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(new User(/* user details */));
    }

    @Test
    void testToggleFollow_AddFollow() throws Exception {
        Long resPk = 1L;

        mockMvc.perform(put("/api/follow/toggle/{res_pk}", resPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(1))
                .andExpect(jsonPath("$.resultMsg").value("좋아요 완료"));
    }

    @Test
    void testToggleFollow_RemoveFollow() throws Exception {
        Long resPk = 1L;

        // 미리 팔로우를 추가
        Restaurant restaurant = restaurantRepository.findById(resPk).orElseThrow();
        resFollowRepository.save(new ResFollow(/* set user and restaurant */));

        mockMvc.perform(put("/api/follow/toggle/{res_pk}", resPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(1))
                .andExpect(jsonPath("$.resultMsg").value("좋아요 취소"));
    }
}