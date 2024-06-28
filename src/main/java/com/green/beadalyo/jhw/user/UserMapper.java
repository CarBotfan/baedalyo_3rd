package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int signUpUser(SignUpPostReq p);
    User signInUser(String userId);
    UserInfoGetRes selProfileUserInfo(UserInfoGetReq p);
    int updProfilePic(UserPicPatchReq p);
    User getUserById(String userId);
}
