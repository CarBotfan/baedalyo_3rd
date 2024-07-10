package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.jhw.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignInPostReq {
    @Schema(defaultValue = "ID")
    private String userId;
    @Schema(defaultValue = "비밀번호")
    private String userPw;
    private Integer userLoginType;
}
