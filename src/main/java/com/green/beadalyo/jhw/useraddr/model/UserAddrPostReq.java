package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAddrPostReq {
    @JsonIgnore
    private long addrPk;
    @JsonIgnore
    private long signedUserId;
    private String addr1;
    private String addr2;
    private BigDecimal addrCoorX;
    private BigDecimal addrCoorY;
}
