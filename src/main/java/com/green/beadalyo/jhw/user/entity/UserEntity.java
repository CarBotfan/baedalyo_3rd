package com.green.beadalyo.jhw.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPk;

    @Column(nullable = false, unique = true)
    private String userId;

    private String userPw;

    private String userName;

    @Column(unique = true)
    private String userNickname;

    private String userPic;

    @Column(length = 30, unique = true)
    private String userPhone;

    @Column(length = 50, unique = true)
    private String userEmail;

    @Column(length = 20, nullable = false)
    private String userRole;

    @ColumnDefault("1")
    @Column(nullable = false)
    private int userState;

    @ColumnDefault("1")
    @Column(nullable = false)
    private Integer userLoginType ;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
