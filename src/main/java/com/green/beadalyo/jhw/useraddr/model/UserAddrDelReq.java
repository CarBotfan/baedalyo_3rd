package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class UserAddrDelReq {
    @JsonIgnore
    private long signedUserPk;
    private long addrPk;
    @ConstructorProperties("addr_pk")
    public UserAddrDelReq (long addrPk) {
        this.addrPk = addrPk;
    }
}
