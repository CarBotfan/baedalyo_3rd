package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    private long userPk;
    private String userName;
    private String userPic;
    private UserAddrGetRes mainAddr;
    private String accessToken;
}
