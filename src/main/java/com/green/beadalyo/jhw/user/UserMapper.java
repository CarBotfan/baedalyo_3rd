package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    long signUpUser(SignUpPostReq p);
    SignInRes signInUser(SignInPostReq p);
    User signInPost(String uid);
    UserInfoGetRes selProfileUserInfo(UserInfoGetReq p);
    int updProfilePic(UserPicPatchReq p);
}
