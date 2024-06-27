package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper mapper;

    @Override
    public int signUpPostReq(MultipartFile pic, SignUpPostReq p) {
        return 0;
    }

    @Override
    public SignInRes signInPost(HttpServletResponse res, SignInPostReq p) {
        return null;
    }

    @Override
    public UserInfoGetRes getUserInfo(UserInfoGetReq p) {
        return null;
    }

    @Override
    public String patchProfilePic(UserPicPatchReq p) {
        return null;
    }

    @Override
    public Map getAccessToken(HttpServletRequest req) {
        return null;
    }
}
