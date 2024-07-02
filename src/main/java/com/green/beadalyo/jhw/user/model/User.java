package com.green.beadalyo.jhw.user.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userPic;
    private int userState;
    private String userRole;
}
