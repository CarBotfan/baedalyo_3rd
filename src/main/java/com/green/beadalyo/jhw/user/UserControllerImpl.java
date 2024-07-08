package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.user.exception.IncorrectPwException;
import com.green.beadalyo.jhw.user.exception.UserNotFoundException;
import com.green.beadalyo.jhw.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "일반 유저 가입")
    public ResultDto<Integer> postUserSignUp(@RequestPart(required = false) MultipartFile pic, @RequestPart UserSignUpPostReq p) {
        int statusCode = 100;
        int result = 0;
        String msg = "가입 성공";
        p.setUserRole("ROLE_USER");
        try {
            result = service.postSignUp(pic, p);
        } catch (UserNotFoundException e) {
            statusCode = 101;
            msg = e.getMessage();
        } catch (IncorrectPwException e) {
            statusCode = 102;
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = 109;
            msg = "백엔드 에러";
        }

        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
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
    @Operation(description = "로그인")
    public ResultDto<SignInRes> postSignIn(HttpServletResponse res, @RequestBody SignInPostReq p) {
        int statusCode = 2;
        SignInRes result = new SignInRes();
        String msg = "로그인 성공";

        try {
            result = service.postSignIn(res, p);
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<SignInRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-nickname")
    @Operation(description = "유저 정보 수정")
    public ResultDto<Integer> patchUserNickname(@RequestBody UserNicknamePatchReq p) {
        int result = service.patchUserNickname(p);
        return ResultDto.<Integer>builder()
                .statusCode(2)
                .resultMsg("변경 완료")
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-phone")
    @Operation(description = "유저 정보 수정")
    public ResultDto<Integer> patchUserPhone(@RequestBody UserPhonePatchReq p) {
        int result = service.patchUserPhone(p);
        return ResultDto.<Integer>builder()
                .statusCode(2)
                .resultMsg("변경 완료")
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-pic")
    @Operation(description = "프로필 이미지 수정")
    public ResultDto<String> patchProfilePic(@RequestPart(required = false) MultipartFile pic, @RequestPart UserPicPatchReq p) {
        int statusCode = 2;
        String result = "";
        String msg = "수정 완료";
        try {
            result = service.patchProfilePic(pic, p);
        } catch(Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<String>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("update-pw")
    @Operation(description = "비밀번호 수정")
    public ResultDto<Integer> patchUserPassword(@RequestBody UserPasswordPatchReq p) {
        int statusCode = 2;
        int result = -1;
        String msg = "수정 완료";
        try {
            result = service.patchUserPassword(p);
        } catch(Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("access-token")
    @Operation(description = "액세스 토큰 발급")
    public ResultDto<Map> getAccessToken(HttpServletRequest req) {
        Map result = service.getAccessToken(req);
        return ResultDto.<Map>builder()
                .statusCode(2)
                .resultMsg("")
                .resultData(result).build();
    }

    @Override
    @GetMapping
    @Operation(description = "유저 정보 조회")
    public ResultDto<UserInfoGetRes> getUserInfo() {
        UserInfoGetRes result = service.getUserInfo();
        return ResultDto.<UserInfoGetRes>builder()
                .statusCode(2)
                .resultMsg("조회 완료")
                .resultData(result).build();
    }

    @Override
    @PostMapping("/normal/delete")
    @Operation(description = "유저 탈퇴")
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
                .resultData(result).build();
    }

    @Override
    public ResultDto<Integer> deleteOwner(OwnerDelReq p) {
        return null;
    }
}
