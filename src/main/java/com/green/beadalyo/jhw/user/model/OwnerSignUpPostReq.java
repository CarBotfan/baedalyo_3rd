package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OwnerSignUpPostReq {
    @JsonIgnore
    private long userPk;
    @Schema(defaultValue = "ID")
    private String userId;
    @Schema(defaultValue = "비밀번호")
    private String userPw;
    @Schema(defaultValue = "비밀번호 확인")
    private String userPwConfirm;

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
    private Integer userLoginType;
    @Schema(defaultValue = "영업 시작 시간")
    private String openTime;
    @Schema(defaultValue = "영업 종료 시간")
    private String closeTime;
    @Schema(defaultValue = "가게 주소")
    private String addr;
    //사업장 설명
    @Schema(defaultValue = "사업장 설명")
    private String desc1 ;
    //리뷰 설명
    @Schema(defaultValue = "리뷰 설명")
    private String desc2 ;
    @Schema(defaultValue = "124.014")
    private Double coorX;
    @Schema(defaultValue = "37.017")
    private Double coorY;
    @Schema(defaultValue = "식당 이름")
    private String restaurantName;
    @Schema(defaultValue = "사업자 등록번호")
    private String regiNum;

}
