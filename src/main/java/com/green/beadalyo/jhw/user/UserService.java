package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    long saveUser(User user) throws Exception;
    int postOwnerSignUp(MultipartFile pic, OwnerSignUpPostReq p);
    int deleteUser(User user) throws Exception;
    int patchUserNickname(UserNicknamePatchReq p) throws Exception;
    int patchUserPhone(UserPhonePatchReq p) throws Exception;
    int patchUserPassword(UserPasswordPatchReq p) throws Exception;
    SignInRes postSignIn(HttpServletResponse res, SignInPostReq p) throws Exception;
    UserInfoGetRes getUserInfo() throws Exception;
    String patchProfilePic(String fileName) throws Exception;
    Map getAccessToken(HttpServletRequest req) throws Exception;
    UserGetRes getUserByPk() throws Exception;
    UserGetRes getUserByPk(long signedUserPk) throws Exception;
    int deleteOwner(User user);
    int duplicatedCheck(String userId);
}
