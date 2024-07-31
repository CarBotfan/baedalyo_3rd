package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserInfoGetRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);
    @Query("select new com.green.beadalyo.jhw.user.model.UserInfoGetRes" +
            "(ue.userId, ue.userName, ue.userNickname, ue.userPic, ue.userPhone, ue.userEmail)" +
            " from User ue WHERE ue.userPk = :userPk")
    UserInfoGetRes findUserInfoByUserPk(Long userPk);


    User findByUserPk(Long userPk);

}
