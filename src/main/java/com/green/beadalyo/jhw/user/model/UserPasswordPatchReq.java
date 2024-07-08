package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordPatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "기존 비밀번호")
    private String userPw;
    @Schema(defaultValue = "변경할 비밀번호")
    private String newPw;
}
