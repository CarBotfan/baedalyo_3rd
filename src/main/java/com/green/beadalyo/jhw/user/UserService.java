package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    long postUserSignUp(User user) throws Exception;
    int deleteUser(User user) throws Exception;
    int patchUserPassword(UserPasswordPatchReq p) throws Exception;
    SignInRes postSignIn(HttpServletResponse res, User user) throws Exception;
    UserInfoGetRes getUserInfo(User user) throws Exception;
    Map getAccessToken(HttpServletRequest req) throws Exception;
    int duplicatedIdCheck(String userId);
}
