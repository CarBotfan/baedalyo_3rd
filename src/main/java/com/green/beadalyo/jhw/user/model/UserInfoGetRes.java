package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.Entity.UserAddr;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private String userPhone;
    @Schema(defaultValue = "이메일")
    private String userEmail;
    @Schema(defaultValue = "메인 주소")
    private UserAddrGetRes mainAddr;
    public UserInfoGetRes(String userId, String userName
            , String userNickname, String userPic
            , String userPhone, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPic = userPic;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }
}
