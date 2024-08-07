package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.jhw.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignInPostReq {
    @Schema(defaultValue = "test_user2")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @Schema(defaultValue = "test1234")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String userPw;
    @JsonIgnore
    private Integer userLoginType;
}
