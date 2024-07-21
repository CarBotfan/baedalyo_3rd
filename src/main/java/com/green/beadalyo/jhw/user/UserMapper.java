package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int signUpUser(UserSignUpPostReq p);
    User signInUser(SignInPostReq p);
    User getUserById(String userId);
    User getUserByPk(long userPk);
    UserInfoGetRes selProfileUserInfo(long signedUserPk) throws Exception;
    int updProfilePic(UserPicPatchReq p) throws Exception;
    int updUserNickname(UserNicknamePatchReq p) throws Exception;
    int updUserPhone(UserPhonePatchReq p) throws Exception;
    int updUserPassword(UserPasswordPatchReq p) throws Exception;
    int deleteUser(long userPk) ;
    UserAddrGetRes getMainAddr(long signedUserPk);
    String getUserPicName(long signedUserPk);
}

