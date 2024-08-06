package com.green.beadalyo.lmy.resfollow.repository;


import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResFollowRepository extends JpaRepository<ResFollow, Long> {
    ResFollow findResFollowByResPkAndUserPk(Restaurant restaurant, User user);
}
