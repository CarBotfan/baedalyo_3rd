package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpPostReq {
    @JsonIgnore
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    @JsonIgnore
    private String userPic;
    private String userPhone;
    private int userRole;
    private int userLoginType;
}
