package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.user.exception.*;
import com.green.beadalyo.jhw.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = " 유저 컨트롤러")
public class UserControllerImpl implements UserController{
    private final UserServiceImpl service;
    private final RestaurantService restaurantService;

    @Override
    @PostMapping(value = "/sign-up", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE
            , MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "일반 유저 회원가입", description = "일반 유저 회원가입을 진행합니다")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -11 : 중복된 닉네임 또는 전화번호 </p>" +
                            "<p> -7 : 중복된 ID </p>" +
                            "<p> -6 : 올바르지 않은 파일 확장자 </p>" +
                            "<p> -5 : 비밀번호 재입력 불일치 </p>" +
                            "<p> -4 : 파일 업로드 실패 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> postUserSignUp(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart UserSignUpPostReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "가입 성공";
        p.setUserRole("ROLE_USER");
        try {
            service.postSignUp(pic, p);
            result = 1;
        } catch (DuplicatedIdException e) {
            statusCode = -7;
            msg = e.getMessage();
        } catch(InvalidRegexException e) {
            msg = e.getMessage();
            statusCode = -6;
        } catch (PwConfirmFailureException e) {
            statusCode = -5;
            msg = e.getMessage();
        } catch(FileUploadFailedException e) {
            statusCode = -4;
            msg = e.getMessage();

        } catch (DuplicatedInfoException e) {
            statusCode = -11;
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }

        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "회원 로그인")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -2 : 해당 유저가 존재하지 않음(탈퇴 or 미가입) </p>" +
                            "<p> -3 : 비밀번호 불일치 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<SignInRes> postSignIn(HttpServletResponse res, @Valid @RequestBody SignInPostReq p) {
        int statusCode = 1;
        SignInRes result = new SignInRes();
        String msg = "로그인 성공";

        try {
            result = service.postSignIn(res, p);
        } catch(UserNotFoundException e) {
           statusCode = -2;
           msg = e.getMessage();
        } catch(IncorrectPwException e) {
            statusCode = -3;
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        if(result.getMainAddr() == null) {
            statusCode = 2;
        }
        return ResultDto.<SignInRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PostMapping(value = "/owner/sign-up", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE
            , MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "음식점 사장 회원가입", description = "음식점 사장 가입을 진행합니다")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -11 : 중복된 닉네임 또는 전화번호 </p>" +
                            "<p> -7 : 중복된 ID </p>" +
                            "<p> -6 : 올바르지 않은 파일 확장자 </p>" +
                            "<p> -5 : 비밀번호 재입력 불일치 </p>" +
                            "<p> -4 : 파일 업로드 실패 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> postOwnerSignUp(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart OwnerSignUpPostReq p) {
        int result = 0;
        String msg = "가입 성공";
        int statusCode = 1;
        p.setUserRole("ROLE_OWNER");
        try {
            result = service.postOwnerSignUp(pic, p);
        } catch (DuplicatedInfoException e) {
            statusCode = -11;
            msg = e.getMessage();
        } catch (DuplicatedIdException e) {
            statusCode = -7;
            msg = e.getMessage();
        } catch(InvalidRegexException e) {
            msg = e.getMessage();
            statusCode = -6;
        } catch(PwConfirmFailureException e) {
            statusCode = -5;
            msg = e.getMessage();
        } catch(FileUploadFailedException e) {
            statusCode = -4;
            msg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-nickname")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "유저 닉네임 수정", description = "유저 닉네임 수정")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -11 : 중복된 닉네임 또는 전화번호 </p>" +
                            "<p> -10 : 수정 실패 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> patchUserNickname(@RequestBody @Valid UserNicknamePatchReq p) {
        int statusCode = 1;
        String msg = "변경 완료";
        int result = 0;
        try {
            result = service.patchUserNickname(p);
        } catch (DuplicatedInfoException e) {
            statusCode = -11;
            msg = e.getMessage();
        } catch(UserPatchFailureException e) {
            msg = e.getMessage();
            statusCode = -10;
        } catch(Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-phone")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "유저 전화번호 수정", description = "유저 전화번호 수정")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -11 : 중복된 닉네임 또는 전화번호 </p>" +
                            "<p> -10 : 수정 실패 </p>" +
                            "<p> -6 : 올바르지 않은 전화번호 형식 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> patchUserPhone(@RequestBody @Valid UserPhonePatchReq p) {
        int statusCode = 1;
        String msg = "변경 완료";
        int result = 0;
        try {
            result = service.patchUserPhone(p);
        } catch(UserPatchFailureException e) {
            msg = e.getMessage();
            statusCode = -10;
        } catch (DuplicatedInfoException e) {
            statusCode = -11;
            msg = e.getMessage();
        } catch(MethodArgumentNotValidException e) {
            msg = e.getMessage();
            statusCode = -6;
        }catch(Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping(value = "/update-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "프로필 이미지 수정", description = "프로필 이미지 수정")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -10 : 수정 실패 </p>" +
                            "<p> -4 : 파일 업로드 실패 </p>" +
                            "<p> -6 : 올바르지 않은 파일 형식 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<String> patchProfilePic(@RequestPart(required = false) MultipartFile pic) {
        int statusCode = 1;
        String result = "";
        String msg = "수정 완료";
        UserPicPatchReq p = new UserPicPatchReq();
        try {
            result = service.patchProfilePic(pic, p);
        } catch(UserPatchFailureException e) {
            msg = e.getMessage();
            statusCode = -10;
        } catch(FileUploadFailedException e) {
            msg = e.getMessage();
            statusCode = -4;
        } catch(MethodArgumentNotValidException e) {
            msg = e.getMessage();
            statusCode = -6;
        } catch(Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<String>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/update-pw")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -2 : 해당 유저를 찾을 수 없음 </p>" +
                            "<p> -3 : 비밀번호 불일치 </p>" +
                            "<p> -5 : 비밀번호 재입력 불일치 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> patchUserPassword(@RequestBody @Valid UserPasswordPatchReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "수정 완료";
        try {
            result = service.patchUserPassword(p);
        } catch(UserNotFoundException e) {
            msg = e.getMessage();
            statusCode = -2;
        } catch(IncorrectPwException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch(PwConfirmFailureException e) {
            msg = e.getMessage();
            statusCode = -5;
        } catch(Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/access-token")
    @Operation(summary = "액세스 토큰 발급",description = "액세스 토큰 발급")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -8 : 쿠키 값이 null임 </p>" +
                            "<p> -9 : 토큰이 만료됨 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Map> getAccessToken(HttpServletRequest req) {
        int statusCode = 1;
        String msg = "발급 성공";
        Map result = new HashMap();
        try { result = service.getAccessToken(req); }
        catch (NullCookieException e) {
            statusCode = -8;
            msg = "쿠키 null";
        } catch(InvalidTokenException e) {
            statusCode = -9;
            msg = "refresh token 만료";
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<Map>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/user-info")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "유저 정보 조회", description = "유저 정보 조회")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -2 : 해당 유저를 찾을 수 없음 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<UserInfoGetRes> getUserInfo() {
        UserInfoGetRes result = new UserInfoGetRes();
        String msg = "조회 성공";
        int statusCode = 1;
        try {result = service.getUserInfo();}
        catch(UserNotFoundException e) {
            msg = e.getMessage();
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            statusCode = -1;
            msg = e.getMessage();
        }
        return ResultDto.<UserInfoGetRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "일반 회원 탈퇴", description = "일반 회원 탈퇴")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -2 : 해당 유저를 찾을 수 없음 </p>" +
                            "<p> -3 : 비밀번호 불일치 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> deleteUser(@RequestBody UserDelReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "탈퇴 완료";
        try {
            result = service.deleteUser(p);
        } catch (UserNotFoundException e) {
            statusCode = -2;
            msg = e.getMessage();
        } catch(IncorrectPwException e) {
            statusCode = -3;
            msg = e.getMessage();
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
    @PostMapping("/owner/delete")
    @PreAuthorize("hasAnyRole('OWNER')")
    @Operation(summary = "음식점 사장 탈퇴", description = "음식점 사장 탈퇴")
    @ApiResponse(
            description =
                    "<p> 1 : 아이디 사용 가능 </p>"+
                            "<p> -2 : 해당 유저를 찾을 수 없음 </p>" +
                            "<p> -3 : 비밀번호 불일치 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> deleteOwner(@RequestBody UserDelReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "탈퇴 완료";
        try {
            result = service.deleteOwner(p);
        } catch (UserNotFoundException e) {
            statusCode = -2;
            msg = e.getMessage();
        } catch(IncorrectPwException e) {
            statusCode = -3;
            msg = e.getMessage();
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
    @GetMapping("/is-duplicated")
    @Operation(summary = "아이디 중복 체크")
    @ApiResponse(
            description =
                    "<p> 1 : 아이디 사용 가능 </p>"+
                            "<p> -7 : 중복된 아이디 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> duplicatedCheck(@RequestParam(name = "user_id") String userId) {
        int statusCode = 1;
        int result = 0;
        String msg = "사용 가능한 ID";
        try {
            result = service.duplicatedCheck(userId);
        } catch (DuplicatedIdException e) {
            statusCode = -7;
            msg = e.getMessage();
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sign-out")
    @Operation(summary = "로그아웃")
    @ApiResponse(
            description =
                    "<p> 1 : 로그아웃 완료 </p>"+
                            "<p> -20 : 인증 정보 없음 </p>" +
                            "<p> -1 : 기타 오류 </p>"
    )
    public ResultDto<Integer> userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String msg = "인증 정보 없음";
        int result = -20;
        if (authentication != null) {
            try {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                service.logoutToken(request, response);
                msg = "로그아웃 완료";
                result = 1;
            } catch (Exception e) {
                msg = e.getMessage();
                result = -1;
            }
        }
        return ResultDto.<Integer>builder()
                .statusCode(result)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
}
