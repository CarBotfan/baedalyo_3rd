package com.green.beadalyo.jhw.email.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.green.beadalyo.common.globalconst.GlobalPattern.userIdPattern;

@Getter
@Setter
public class EmailFindRequestDto {
    @NotBlank(message = "이름")
    @NotBlank(message = "이름를 입력해주세요.")
    @Schema(defaultValue = "이름")
    private String userName;
    @Email(message = "이메일 형식을 지켜주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @Schema(defaultValue = "아이디")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = userIdPattern, message = "아이디는 8자 이상이어야 합니다.")
    private  String userId;
}
