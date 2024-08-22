package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.green.beadalyo.common.globalconst.GlobalPattern.userEmailPattern;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindUserIdReq {
    @NotBlank(message = "이름")
    @NotBlank(message = "이름를 입력해주세요.")
    @Schema(defaultValue = "이름")
    private String userName;

    @Schema(defaultValue = "이메일")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = userEmailPattern, message = "유효하지 않은 형식의 이메일입니다.")
    private String userEmail;

//    @Schema(defaultValue = "인증번호 재인증")
//    @NotEmpty(message = "인증번호를 입력하세요.")
//    private String authNum;
}
