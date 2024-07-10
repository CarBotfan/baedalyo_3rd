package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.jhw.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSignUpPostReq {
    @JsonIgnore
    private long userPk;
    @Schema(defaultValue = "ID")
    private String userId;
    @Schema(defaultValue = "비밀번호")
    private String userPw;
    @Schema(defaultValue = "비밀번호 확인")
    private String userPwConfirm;
    @Schema(defaultValue = "이름")
    private String userName;
    @Schema(defaultValue = "닉네임")
    private String userNickname;
    @JsonIgnore
    private String userPic;
    @Schema(defaultValue = "전화번호")
    private String userPhone;
    @JsonIgnore
    private String userRole;
    @JsonIgnore
    private Integer userLoginType;
}
