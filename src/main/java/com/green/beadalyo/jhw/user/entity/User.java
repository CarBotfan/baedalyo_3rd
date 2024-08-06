package com.green.beadalyo.jhw.user.entity;

import com.green.beadalyo.jhw.user.model.UserInfoPatchDto;
import com.green.beadalyo.jhw.user.model.UserSignUpPostReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private Long userPk;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_pw")
    private String userPw;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_nickname", unique = true)
    private String userNickname;

    @Column(name = "user_pic")
    private String userPic;

    @Column(name = "user_phone" ,length = 30, unique = true)
    private String userPhone;

    @Column(name = "user_email", length = 50, unique = true)
    private String userEmail;

    @Column(name = "user_role", length = 20)
    private String userRole;

    @ColumnDefault("1")
    @Column(name = "user_state")
    private Integer userState;

    @ColumnDefault("1")
    @Column(name = "user_login_type")
    private Integer userLoginType ;

    @Column(name = "mileage")
    private Integer mileage ;

    @Column(name = "user_block_date")
    private LocalDate userBlockDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(UserSignUpPostReq p) {
        this.userId = p.getUserId();
        this.userPw = p.getUserPw();
        this.userName = p.getUserName();
        this.userNickname = p.getUserNickname();
        this.userPhone = p.getUserPhone();
        this.userEmail = p.getUserEmail();
        this.userRole = p.getUserRole();
        this.userState = 1;
        this.userLoginType = p.getUserLoginType();
    }

    public void update(UserInfoPatchDto p) {
        if(p.getUserNickname() != null && !p.getUserNickname().isEmpty()) {
            this.userNickname = p.getUserNickname();
        }
        if(p.getUserPhone() != null && !p.getUserPhone().isEmpty()) {
            this.userPhone = p.getUserPhone();
        }
        if(p.getUserPic() != null && !p.getUserPic().isEmpty()) {
            this.userPic = p.getUserPic();
        }
    }

}
