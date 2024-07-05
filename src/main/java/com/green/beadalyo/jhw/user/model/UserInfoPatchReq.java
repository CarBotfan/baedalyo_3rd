package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoPatchReq {
    @JsonIgnore
    private long signedUserPk;
    private String userNickname;
    private String userPhone;
}
