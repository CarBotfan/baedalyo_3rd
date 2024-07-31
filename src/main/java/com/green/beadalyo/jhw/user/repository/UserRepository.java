package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserInfoGetRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);

    User findByUserPk(Long userPk);

}
