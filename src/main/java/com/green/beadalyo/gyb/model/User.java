package com.green.beadalyo.gyb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

//Mock Data
@Table(name = "user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private Long seq ;

    @Column(name = "user_id")
    private String userId ;
    @Column(name = "user_pw")
    private String userPw ;
    @Column(name = "user_name")
    private String userName ;
    @Column(name = "user_nickname")
    private String userNickname ;
    @Column(name = "user_pic")
    private String userPic ;
    @Column(name = "user_phone")
    private String userPhone ;
    @Column(name = "user_role")
    @ColumnDefault("role_user")
    private String userRole ;
    @Column(name = "user_state")
    private Integer userState ;
    @Column(name = "user_login_type")
    private Integer userLoginType ;
    @Column(name = "created_at")
    private LocalDateTime createdAt ;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt ;

//    public User()
//    {
//        this.seq = 1L ;
//        this.userId = "gsb7080" ;
//        this.userPw = "asdf" ;
//        this.userName = "공영빈" ;
//        this.userNickname = "스토마게돈" ;
//        this.userPic = null ;
//        this.userPhone = "010-1244-4815" ;
//        this.userRole = "ROLE_OWNER" ;
//        this.userState = 1 ;
//        this.userLoginType = 1 ;
//    }
//
//    public User Admin()
//    {
//        this.seq = 2L ;
//        this.userId = "gsb7080" ;
//        this.userPw = "asdf" ;
//        this.userName = "김관리자" ;
//        this.userNickname = "관리자마게돈" ;
//        this.userPic = null ;
//        this.userPhone = "010-1244-4815" ;
//        this.userRole = "ROLE_ADMIN" ;
//        this.userState = 1 ;
//        this.userLoginType = 1 ;
//        return this ;
//    }
}
