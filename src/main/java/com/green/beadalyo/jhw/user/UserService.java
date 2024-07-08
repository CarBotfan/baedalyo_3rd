package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    long postSignUp(MultipartFile pic, UserSignUpPostReq p);
    int deleteUser(UserDelReq p);
    int patchUserNickname(UserNicknamePatchReq p);
    int patchUserPhone(UserPhonePatchReq p);
    int patchUserPassword(UserPasswordPatchReq p);
    SignInRes postSignIn(HttpServletResponse res, SignInPostReq p);
    UserInfoGetRes getUserInfo();
    String patchProfilePic(MultipartFile pic, UserPicPatchReq p);
    Map getAccessToken(HttpServletRequest req);
    User getUserByPk();
    User getUserByPk(long signedUserPk);
}
