package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserInfoGetRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);

    Boolean existsByUserEmail(String userEmail);
    Boolean existsByUserNickname(String userNickname);
    Boolean existsByUserPhone(String userPhone);
    Boolean existsByUserId(String userId);
    User findByUserPk(Long userPk);
    User findByUserEmailAndUserName(String userEmail, String userName);
    User findByUserEmailAndUserNameAndUserId(String userEmail, String userName, String userId);
}
