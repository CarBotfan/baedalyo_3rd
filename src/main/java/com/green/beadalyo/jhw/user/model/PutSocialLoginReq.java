package com.green.beadalyo.jhw.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PutSocialLoginReq {
    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(defaultValue = "이름")
    private String userName;
    @Schema(defaultValue = "전화번호")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "올바르지 않은 형식의 전화번호입니다.")
    private String userPhone;
}
