package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNicknamePatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "변경할 닉네임")
    private String userNickname;
}
