package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPhonePatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "변경할 전화번호")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "올바르지 않은 형식의 전화번호입니다.")
    private String userPhone;
}
