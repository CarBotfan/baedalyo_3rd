package com.green.beadalyo.jhw.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = null;

        if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
        }
        return myUserDetails == null ? null : myUserDetails.getMyUser() ;
    }

    public long getLoginUserPk() {
        MyUser myUser = getLoginUser();
        return myUser == null ? 0 : myUser.getUserPk();
    }
}
