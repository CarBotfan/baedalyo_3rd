package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.common.model.ResultDto;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserController {
    ResultDto<Integer> postUserSignUp(MultipartFile pic, UserSignUpPostReq p);
    ResultDto<Integer> postOwnerSignUp(MultipartFile pic, OwnerSignUpPostReq p);
    ResultDto<SignInRes> postSignIn(SignInPostReq p);
    ResultDto<String> patchProfilePic(MultipartFile pic, UserPicPatchReq p);
    ResultDto<Integer> patchUserInfo(UserInfoPatchReq p);
    ResultDto<Map> getAccessToken(HttpServletRequest req);
    ResultDto<UserInfoGetRes> getUserInfo(UserInfoGetReq p);
}
