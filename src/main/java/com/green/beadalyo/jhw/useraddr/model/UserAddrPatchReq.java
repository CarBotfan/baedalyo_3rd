package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserAddrPatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "1")
    private long addrPk;
    @Schema(defaultValue = "시/군/구")
    private String addr1;
    @Schema(defaultValue = "상세주소")
    private String addr2;
    @Schema(defaultValue = "124.014")
    private BigDecimal addrCoorX;
    @Schema(defaultValue = "36.746")
    private BigDecimal addrCoorY;

}
