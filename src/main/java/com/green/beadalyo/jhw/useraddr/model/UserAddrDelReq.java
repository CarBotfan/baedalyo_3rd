package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddrDelReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "1")
    private long userAddrPk;
}
