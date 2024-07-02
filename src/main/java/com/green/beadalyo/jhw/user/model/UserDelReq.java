package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDelReq {
    @JsonIgnore
    private long signedUserPk;
    private String userPw;
}
