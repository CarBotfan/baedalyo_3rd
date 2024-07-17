package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserNicknamePatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "변경할 닉네임")
    @NotBlank(message = "변경할 닉네임을 입력해주세요.")
    private String userNickname;
}
