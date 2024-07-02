package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerSignUpPostReq {
    @JsonIgnore
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    @JsonIgnore
    private String userPic;
    private String userPhone;
    @JsonIgnore
    private int userRole;
    @JsonIgnore
    private int userLoginType;
    private String openTime;
    private String closeTime;
    private String addr;
    private String coorX;
    private String coorY;
    private String restaurantName;
    private String regiNum;

}
