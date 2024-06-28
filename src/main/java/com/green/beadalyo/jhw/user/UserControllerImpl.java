package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.common.model.ResultDto;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserControllerImpl implements UserController{
    private final UserServiceImpl service;

    @Override
    public ResultDto<Integer> postSignUp(MultipartFile pic, SignUpPostReq p) {
        return null;
    }

    @Override
    public ResultDto<SignInRes> postSignIn(SignInPostReq p) {
        return null;
    }

    @Override
    public ResultDto<Integer> patchUserInfo(UserInfoPatchReq p) {
        return null;
    }

    @Override
    public ResultDto<String> patchProfilePic(UserPicPatchReq p) {
        return null;
    }

    @Override
    public ResultDto<Map> getAccessToken(HttpServletRequest req) {
        return null;
    }

    @Override
    public ResultDto<UserInfoGetRes> getUserInfo(UserInfoGetReq p) {
        return null;
    }
}
