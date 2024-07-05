package com.green.beadalyo.jhw.useraddr.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddrGetRes {
    @Schema(defaultValue = "시/군/구")
    private String addr1;
    @Schema(defaultValue = "상세주소")
    private String addr2;
    @Schema(defaultValue = "124.014")
    private float addrCoorX;
    @Schema(defaultValue = "36.746")
    private float addrCoorY;
}
