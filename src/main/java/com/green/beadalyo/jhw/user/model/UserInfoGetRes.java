package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoGetRes {
    private String userId;
    private String userName;
    private String userPic;
    private String userPhone;
    private UserAddrGetRes address;
}
