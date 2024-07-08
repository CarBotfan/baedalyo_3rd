package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserController {
    ResultDto<Integer> postUserSignUp(MultipartFile pic, UserSignUpPostReq p);
    ResultDto<Integer> postOwnerSignUp(MultipartFile pic, OwnerSignUpPostReq p);
    ResultDto<SignInRes> postSignIn(HttpServletResponse res, SignInPostReq p);
    ResultDto<String> patchProfilePic(MultipartFile pic, UserPicPatchReq p);
    ResultDto<Integer> patchUserNickname(UserNicknamePatchReq p);
    ResultDto<Integer> patchUserPhone(UserPhonePatchReq p);
    ResultDto<Integer> patchUserPassword(UserPasswordPatchReq p);
    ResultDto<Map> getAccessToken(HttpServletRequest req);
    ResultDto<UserInfoGetRes> getUserInfo();
    ResultDto<Integer> deleteUser(UserDelReq p);
    ResultDto<Integer> deleteOwner(OwnerDelReq p);
}
