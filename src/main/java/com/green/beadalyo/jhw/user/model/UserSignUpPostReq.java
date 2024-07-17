package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.jhw.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^.{8,}$", message = "아이디는 8자 이상이어야 합니다.")
    private String userId;
    @Schema(defaultValue = "비밀번호")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?]).{8,}$"
            , message = "비밀번호는 특수문자와 숫자를 포함한 8자 이상이어야 합니다.")
    private String userPw;
    @Schema(defaultValue = "비밀번호 확인")
    private String userPwConfirm;
    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(defaultValue = "이름")
    private String userName;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Schema(defaultValue = "닉네임")
    private String userNickname;
    @JsonIgnore
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+(\\.[a-zA-Z0-9_.-]+)*\\.(?i)(jpg|jpeg|png|gif|bmp|tiff|svg|webp)$")
    private String userPic;
    @Schema(defaultValue = "전화번호")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "올바르지 않은 형식의 전화번호입니다.")
    private String userPhone;
    @Schema(defaultValue = "이메일")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "유효하지 않은 형식의 이메일입니다.")
    private String userEmail;
    @JsonIgnore
    private String userRole;
    @JsonIgnore
    private Integer userLoginType;
}
