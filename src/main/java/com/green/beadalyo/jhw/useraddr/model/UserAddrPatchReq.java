package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddrPatchReq {
    @JsonIgnore
    private long signedUserId;
    private String addr1;
    private String addr2;
    private String addrZip;
    private float addrCoorX;
    private float addrCoorY;

}
