package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.common.model.ResultDto;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserControllerImpl implements UserController{
    private final UserServiceImpl service;

    @Override
    @PostMapping("/normal/sign-up")
    public ResultDto<Integer> postUserSignUp(@RequestPart(required = false) MultipartFile pic, @RequestPart UserSignUpPostReq p) {
        int statusCode = 2;
        int result = -1;
        String msg = "가입 성공";
        try {
            p.setUserRole("ROLE_USER");
            result = service.postSignUp(pic, p);
        } catch (Exception e) {
            statusCode = -1;
            String resultMsg = e.getMessage();
        }

        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .result(result).build();
    }

    @Override
    public ResultDto<Integer> postOwnerSignUp(@RequestPart(required = false) MultipartFile pic,@RequestPart OwnerSignUpPostReq p) {
        UserSignUpPostReq req = UserSignUpPostReq.builder()
                .userId(p.getUserId())
                .userPw(p.getUserPw())
                .userName(p.getUserName())
                .userNickname(p.getUserNickName())
                .userPhone(p.getUserNickName())
                .userRole("ROLE_OWNER")
                .build();
        service.postSignUp(pic, req);
        return null;
    }

    @Override
    @PostMapping("/sign-in")
    public ResultDto<SignInRes> postSignIn(HttpServletResponse res, @RequestBody SignInPostReq p) {
        int statusCode = 2;
        SignInRes result = new SignInRes();
        String msg = "로그인 성공";

        try {
            result = service.postSignIn(res, p);
        } catch (Exception e) {
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<SignInRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .result(result).build();
    }

    @Override
    @PatchMapping("/update-info")
    public ResultDto<Integer> patchUserInfo(@RequestBody UserInfoPatchReq p) {
        int result = service.patchUserInfo(p);
        return ResultDto.<Integer>builder()
                .result(2)
                .resultMsg("변경 완료")
                .result(result).build();
    }

    @Override
    @PatchMapping("/update-pic")
    public ResultDto<String> patchProfilePic(@RequestPart(required = false) MultipartFile pic, @RequestPart UserPicPatchReq p) {
        int statusCode = 2;
        String result = "";
        String msg = "수정 완료";
        try {
            result = service.patchProfilePic(pic, p);
        } catch(Exception e) {
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<String>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .result(result).build();
    }

    @Override
    @PatchMapping("update-pw")
    public ResultDto<Integer> patchUserPassword(@RequestBody UserPasswordPatchReq p) {
        int statusCode = 2;
        int result = -1;
        String msg = "수정 완료";
        try {
            result = service.patchUserPassword(p);
        } catch(Exception e) {
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .result(result).build();
    }

    @Override
    @GetMapping("access-token")
    public ResultDto<Map> getAccessToken(HttpServletRequest req) {
        Map result = service.getAccessToken(req);
        return ResultDto.<Map>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @GetMapping
    public ResultDto<UserInfoGetRes> getUserInfo() {
        UserInfoGetRes result = service.getUserInfo();
        return ResultDto.<UserInfoGetRes>builder()
                .statusCode(2)
                .resultMsg("조회 완료")
                .result(result).build();
    }

    @Override
    @PostMapping("/normal/delete")
    public ResultDto<Integer> deleteUser(@RequestBody UserDelReq p) {
        int statusCode = 2;
        int result = -1;
        String msg = "탈퇴 완료";
        try {
            result = service.deleteUser(p);
        } catch (Exception e) {
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .result(result).build();
    }

    @Override
    public ResultDto<Integer> deleteOwner(OwnerDelReq p) {
        return null;
    }
}
