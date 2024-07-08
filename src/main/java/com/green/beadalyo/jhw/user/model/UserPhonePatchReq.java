package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPhonePatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "변경할 전화번호")
    private String userPhone;
}
