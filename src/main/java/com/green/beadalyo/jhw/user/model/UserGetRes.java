package com.green.beadalyo.jhw.user.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class UserGetRes {
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickname ;
    private String userPic;
    private String userPhone;
    private String userEmail;
    private String userRole;
    private int userState;
    private Integer userLoginType ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
