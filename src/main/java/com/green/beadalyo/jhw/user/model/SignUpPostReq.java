package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SignUpPostReq {
    @JsonIgnore
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    private String userPic;
    private String userPhone;
}
