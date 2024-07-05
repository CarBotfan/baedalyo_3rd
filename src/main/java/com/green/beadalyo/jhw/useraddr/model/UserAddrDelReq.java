package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddrDelReq {
    @JsonIgnore
    private long signedUserPk;
    private long userAddrPk;
}
