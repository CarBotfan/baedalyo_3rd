package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.UserEntity;
import com.green.beadalyo.jhw.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository2 extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByUserId(String userId);
}
