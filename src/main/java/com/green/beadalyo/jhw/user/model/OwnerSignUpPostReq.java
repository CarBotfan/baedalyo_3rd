package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerSignUpPostReq {
    @JsonIgnore
    private long userPk;
    @Schema(defaultValue = "ID")
    private String userId;
    @Schema(defaultValue = "비밀번호")
    private String userPw;
    @Schema(defaultValue = "이름")
    private String userName;
    @Schema(defaultValue = "닉네임")
    private String userNickName;
    @JsonIgnore
    private String userPic;
    @Schema(defaultValue = "전화번호")
    private String userPhone;
    @JsonIgnore
    private int userRole;
    @JsonIgnore
    private int userLoginType;
    @Schema(defaultValue = "영업 시작 시간")
    private String openTime;
    @Schema(defaultValue = "영업 종료 시간")
    private String closeTime;
    @Schema(defaultValue = "가게 주소")
    private String addr;
    @Schema(defaultValue = "124.014")
    private String coorX;
    @Schema(defaultValue = "37.017")
    private String coorY;
    @Schema(defaultValue = "식당 이름")
    private String restaurantName;
    @Schema(defaultValue = "사업자 등록번호")
    private String regiNum;

}
