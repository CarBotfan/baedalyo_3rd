package com.green.beadalyo.jhw.user.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    private long userPk;
    private String userName;
    private String userPic;
    private String accessToken;
}
