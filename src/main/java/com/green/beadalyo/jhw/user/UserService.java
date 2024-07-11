package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    long postSignUp(MultipartFile pic, UserSignUpPostReq p) throws Exception;
    int deleteUser(UserDelReq p) throws Exception;
    int patchUserNickname(UserNicknamePatchReq p) throws Exception;
    int patchUserPhone(UserPhonePatchReq p) throws Exception;
    int patchUserPassword(UserPasswordPatchReq p) throws Exception;
    SignInRes postSignIn(HttpServletResponse res, SignInPostReq p) throws Exception;
    UserInfoGetRes getUserInfo() throws Exception;
    String patchProfilePic(MultipartFile pic, UserPicPatchReq p) throws Exception;
    Map getAccessToken(HttpServletRequest req) throws Exception;
    User getUserByPk() throws Exception;
    User getUserByPk(long signedUserPk) throws Exception;
    int deleteOwner(UserDelReq p);
    int duplicatedCheck(String userId);
}
