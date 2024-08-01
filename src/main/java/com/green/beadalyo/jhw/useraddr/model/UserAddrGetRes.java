package com.green.beadalyo.jhw.useraddr.model;

import com.green.beadalyo.jhw.useraddr.Entity.UserAddr;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserAddrGetRes {
    @Schema(defaultValue = "1")
    private long addrPk;
    @Schema(defaultValue = "주소 명칭")
    private String addrName;
    @Schema(defaultValue = "시/군/구")
    private String addr1;
    @Schema(defaultValue = "상세주소")
    private String addr2;
    @Schema(defaultValue = "124.014")
    private BigDecimal addrCoorX;
    @Schema(defaultValue = "36.746")
    private BigDecimal addrCoorY;

    public UserAddrGetRes(UserAddr userAddr) {
        this.addrPk = userAddr.getAddrPk();
        this.addrName = userAddr.getAddrName();
        this.addr1 = userAddr.getAddr1();
        this.addr2 = userAddr.getAddr2();
        this.addrCoorX = userAddr.getAddrCoorX();
        this.addrCoorY = userAddr.getAddrCoorY();
    }
}
