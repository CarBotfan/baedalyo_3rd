package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.green.beadalyo.common.globalconst.GlobalPattern.*;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindUserPwReq {
    @NotBlank(message = "이름")
    @NotBlank(message = "이름를 입력해주세요.")
    @Schema(defaultValue = "이름")
    private final String userName;

    @Schema(defaultValue = "이메일")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = userEmailPattern, message = "유효하지 않은 형식의 이메일입니다.")
    private final String userEmail;

    @Schema(defaultValue = "아이디")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = userIdPattern, message = "아이디는 8자 이상이어야 합니다.")
    private final String userId;

    @Schema(defaultValue = "인증번호 재인증")
    @NotEmpty(message = "인증번호를 입력하세요.")
    private final String authNum;

    @Schema(defaultValue = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = userPwPattern, message = "특수문자와 숫자를 포함한 8자 이상이어야 합니다.")
    private final String userPw;
    @Schema(defaultValue = "비밀번호 확인")
    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private final String userPwConfirm;
}
