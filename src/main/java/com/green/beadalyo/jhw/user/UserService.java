package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    int postSignUp(MultipartFile pic, UserSignUpPostReq p);
    SignInRes postSignIn(HttpServletResponse res, SignInPostReq p);
    UserInfoGetRes getUserInfo(UserInfoGetReq p);
    String patchProfilePic(MultipartFile pic, UserPicPatchReq p);
    Map getAccessToken(HttpServletRequest req);
}
