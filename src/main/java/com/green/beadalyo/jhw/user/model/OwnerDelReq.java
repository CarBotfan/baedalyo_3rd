package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerDelReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "비밀번호")
    private String userPw;
}
