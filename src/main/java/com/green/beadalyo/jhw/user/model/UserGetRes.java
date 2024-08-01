package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.user.entity.User;
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

    public UserGetRes(User user) {
        this.userPk = user.getUserPk();
        this.userId = user.getUserId();
        this.userPw = user.getUserPw();
        this.userName = user.getUserName();
        this.userNickname = user.getUserNickname();
        this.userPic = user.getUserPic();
        this.userPhone = user.getUserPhone();
        this.userEmail = user.getUserEmail();
        this.userRole = user.getUserRole();
        this.userState = user.getUserState();
        this.userLoginType = user.getUserLoginType();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
