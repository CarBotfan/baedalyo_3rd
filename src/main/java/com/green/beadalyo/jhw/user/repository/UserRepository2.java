package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.UserEntity;
import com.green.beadalyo.jhw.user.model.User;
import com.green.beadalyo.jhw.user.model.UserInfoGetRes;
import com.green.beadalyo.jhw.user.model.UserPicPatchReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository2 extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByUserId(String userId);
    @Query("select new com.green.beadalyo.jhw.user.model.UserInfoGetRes" +
            "(ue.userId, ue.userName, ue.userNickname, ue.userPic, ue.userPhone, ue.userEmail)" +
            " from UserEntity ue WHERE ue.userPk = :userPk")
    UserInfoGetRes findUserInfoByUserPk(Long userPk);
    @Query("select ue.userPic from UserEntity ue WHERE ue.userPk = :userPk")
    String findUserPicByUserPk(Long userPk);

    UserEntity findByUserPk(Long userPk);

    @Modifying
    @Query("update UserEntity ue set ue.userPic = :userPic where ue.userPk = :userPk")
    int updateUserPic(String userPic, Long userPk);

    @Modifying
    @Query("update UserEntity ue set ue.userNickname = :userNickname where ue.userPk = :userPk")
    int updateUserNickname(String userNickname, Long userPk);

    @Modifying
    @Query("update UserEntity ue set ue.userPw = :userPw where ue.userPk = :userPk")
    int updateUserPassword(String userPw, Long userPk);

    @Modifying
    @Query("update UserEntity ue set ue.userState = 3 where ue.userPk = :userPk")
    int deleteUser(Long userPk);
}
