package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    @Schema(defaultValue = "닉네임")
    private String userNickname;
    @Schema(defaultValue = "메인 주소")
    private UserAddrGetRes mainAddr;
    @Schema(defaultValue = "유저 역할")
    private String userRole;
    @Schema(defaultValue = "액세스 토큰")
    private String accessToken;
}
