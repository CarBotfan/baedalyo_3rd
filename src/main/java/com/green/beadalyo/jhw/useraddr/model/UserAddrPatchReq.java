package com.green.beadalyo.jhw.useraddr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAddrPatchReq {
    @JsonIgnore
    private long signedUserPk;
    private long addrPk;
    private String addrName;
    @NotBlank(message = "주소를 입력해주세요.")
    private String addr1;
    @NotBlank(message = "상세 주소를 입력해주세요.")
    private String addr2;
    private BigDecimal addrCoorX;
    private BigDecimal addrCoorY;

}
