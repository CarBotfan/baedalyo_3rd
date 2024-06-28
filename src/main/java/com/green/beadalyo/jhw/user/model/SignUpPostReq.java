package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpPostReq {
    @JsonIgnore
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    @JsonIgnore
    private String userPic;
    private String userPhone;
    private String userRole;
    private int userLoginType;
}
