package com.green.beadalyo.gyb;

import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.ResultError;
import com.green.beadalyo.jhw.user.UserService;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.SignInRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "로그인")
@RequiredArgsConstructor
@Slf4j
public class TestUserLogin
{

    private final UserServiceImpl service ;

    @GetMapping("/api/sign-in_test")
    @Operation(summary = "테스트용 Swagger PK로 로그인(프론트에 적용 절대 금지)")
    public Result loginTest(HttpServletResponse res, long pk)
    {
        try {
            User user = service.getUser(pk) ;
            SignInRes in = service.postSignIn(res,user) ;
            return ResultDto.builder().resultData(in).build();

        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }

}
