package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoGetRes {
    @Schema(defaultValue = "아이디")
    private String userId;
    @Schema(defaultValue = "이름")
    private String userName;
    @Schema(defaultValue = "닉네임")
    private String userNickname;
    @Schema(defaultValue = "사진 파일명")
    private String userPic;
    @Schema(defaultValue = "전화번호")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    private String userPhone;
    @Schema(defaultValue = "메인 주소")
    private UserAddrGetRes mainAddr;
}
