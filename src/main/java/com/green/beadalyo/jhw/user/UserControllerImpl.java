package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.user.exception.*;
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
    private final RestaurantService restaurantService;

    @Override
    @PostMapping("/normal/sign-up")
    @Operation(description = "일반 유저 가입")
    public ResultDto<Integer> postUserSignUp(@RequestPart(required = false) MultipartFile pic, @RequestPart UserSignUpPostReq p) {
        int statusCode = 100;
        int result = 0;
        String msg = "가입 성공";
        p.setUserRole("ROLE_USER");
        try {
            service.postSignUp(pic, p);
            result = 1;
        } catch (DuplicatedIdException e) {
            statusCode = 101;
            msg = e.getMessage();
        } catch(FileUploadFailedException e) {
            statusCode = 104;
            msg = e.getMessage();

        } catch (PwConfirmFailureException e) {
          statusCode = 105;
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
        int result = 0;
        String msg = "가입 성공";
        int statusCode = -1;
        try {
            UserSignUpPostReq req = UserSignUpPostReq.builder()
                    .userId(p.getUserId())
                    .userPw(p.getUserPw())
                    .userName(p.getUserName())
                    .userNickname(p.getUserNickName())
                    .userPhone(p.getUserNickName())
                    .userRole("ROLE_OWNER")
                    .build();
            long userPk = service.postSignUp(pic, req);
            User user = service.getUserByPk(userPk);
            RestaurantInsertDto dto = new RestaurantInsertDto();
            dto.setUser(user);
            dto.setName(p.getRestaurantName());
            dto.setRegiNum(p.getRegiNum());
            dto.setResAddr(p.getAddr());
            dto.setOpenTime(p.getOpenTime());
            dto.setCloseTime(p.getCloseTime());
            dto.setResCoorX(p.getCoorX());
            dto.setResCoorY(p.getCoorY());
            restaurantService.insertRestaurantData(dto);
            statusCode = 100;
            result = 1;
        } catch (DuplicatedIdException e) {
            statusCode = 101;
            msg = e.getMessage();
        } catch(FileUploadFailedException e) {
            statusCode = 104;
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
    @PostMapping("/sign-in")
    @Operation(description = "로그인")
    public ResultDto<SignInRes> postSignIn(HttpServletResponse res, @RequestBody SignInPostReq p) {
        int statusCode = 100;
        SignInRes result = new SignInRes();
        String msg = "로그인 성공";

        try {
            result = service.postSignIn(res, p);
        } catch(UserNotFoundException e) {
           statusCode = 102;
           msg = e.getMessage();
        } catch(IncorrectPwException e) {
            statusCode = 103;
            msg = e.getMessage();
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
    @Operation(description = "유저 닉네임 수정")
    public ResultDto<Integer> patchUserNickname(@RequestBody UserNicknamePatchReq p) {
        int statusCode = 100;
        String msg = "변경 완료";
        int result = 0;
        try {
            result = service.patchUserNickname(p);
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
    @PatchMapping("/update-phone")
    @Operation(description = "유저 전화번호 수정")
    public ResultDto<Integer> patchUserPhone(@RequestBody UserPhonePatchReq p) {
        int statusCode = 100;
        String msg = "변경 완료";
        int result = 0;
        try {
            result = service.patchUserPhone(p);
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
    @PatchMapping("/update-pic")
    @Operation(description = "프로필 이미지 수정")
    public ResultDto<String> patchProfilePic(@RequestPart(required = false) MultipartFile pic, @RequestPart UserPicPatchReq p) {
        int statusCode = 2;
        String result = "";
        String msg = "수정 완료";
        try {
            result = service.patchProfilePic(pic, p);
        } catch(FileUploadFailedException e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = 104;
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
