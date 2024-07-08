package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    @Schema(defaultValue = "1")
    private long userPk;
    @Schema(defaultValue = "닉네임")
    private String userNickname;
    @Schema(defaultValue = "사진파일 이름")
    private String userPic;
    @Schema(defaultValue = "메인 주소")
    private UserAddrGetRes mainAddr;
    @Schema(defaultValue = "액세스 토큰")
    private String accessToken;
}
