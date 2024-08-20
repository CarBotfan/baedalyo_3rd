package com.green.beadalyo.jhw.security;

import com.green.beadalyo.jhw.security.MyUser;
import lombok.Getter;

@Getter
public class MyUserOAuth2Vo extends MyUser {
    private final String nm;
    private final String pic;
    private final String email;

    public MyUserOAuth2Vo(long userId, String role, String nm, String pic, String email) {
        super(userId, role);
        this.nm = nm;
        this.pic = pic;
        this.email = email;
    }

}

