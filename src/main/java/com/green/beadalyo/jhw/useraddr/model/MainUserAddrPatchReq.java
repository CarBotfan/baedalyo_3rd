package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainUserAddrPatchReq {
    @JsonIgnore
    private long signedUserPk;
    private long changeAddrPk;
}
