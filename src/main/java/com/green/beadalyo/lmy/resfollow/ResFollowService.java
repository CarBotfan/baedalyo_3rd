package com.green.beadalyo.lmy.resfollow;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import com.green.beadalyo.lmy.resfollow.repository.ResFollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ResFollowService {
    private final RestaurantRepository restaurantRepository;
    private final ResFollowRepository resFollowRepository;

    public Restaurant getRes(Long seq){
        return restaurantRepository.findRestaurantBySeq(seq);
    }

    public ResFollow getResFollow(Restaurant res, User user) {
        return resFollowRepository.findResFollowByResPkAndUserPk(res, user);
    }

    public Integer saveResFollow(Restaurant res, User user) {
        ResFollow resFollow = new ResFollow();
        resFollow.setResPk(res);
        resFollow.setUserPk(user);
        resFollowRepository.save(resFollow);
        return 1;
    }

    public Integer deleteResFollow(ResFollow resFollow) {
        resFollowRepository.delete(resFollow);
        return 0;
    }

}
