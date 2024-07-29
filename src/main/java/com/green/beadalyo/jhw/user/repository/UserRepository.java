package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserSignUpPostReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = "INSERT into user" +
            "        SET user_id = #{q.userId}" +
            "        , user_pw = #{q.userPw}" +
            "        , user_name = #{q.userName}" +
            "        , user_nickname = #{q.userNickname}" +
            "        , user_pic = #{q.userPic}" +
            "        , user_phone = #{q.userPhone}" +
            "        , user_email = #{q.userEmail}" +
            "        , user_role = #{q.userRole}" +
            "        , user_login_type = #{q.userLoginType}")
    int signUpUser(UserSignUpPostReq q) ;


}
