package com.green.beadalyo.jhw.user.model;

import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private long userPk;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickname ;
    private String userPic;
    private String userPhone;
    private String userRole;
    private int userState;
    private Integer userLoginType ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
