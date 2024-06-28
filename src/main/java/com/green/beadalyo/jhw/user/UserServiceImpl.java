package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.common.AppProperties;
import com.green.beadalyo.jhw.common.CookieUtils;
import com.green.beadalyo.jhw.common.CustomFileUtils;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import com.green.beadalyo.jhw.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;


    @Override
    @Transactional
    public int signUpPostReq(MultipartFile pic, SignUpPostReq p) {
        String fileName = customFileUtils.makeRandomFileName(pic);
        p.setUserPic(fileName);

        String password = passwordEncoder.encode(p.getUserPw());
        p.setUserPw(password);
        int result = mapper.signUpUser(p);
        if(pic == null) {
            return result;
        }
        try {
            String path = String.format("user/%d", p.getUserPk());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s", path, fileName);
            customFileUtils.transferTo(pic, target);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 실패");
        }
        return result;
    }

    @Override
    public SignInRes signInPost(HttpServletResponse res, SignInPostReq p) {
        User user = mapper.signInUser(p.getUserId());

        if(user == null) {
            throw new RuntimeException();
        }
        if(BCrypt.checkpw(p.getUserPw(), user.getUserPw())) {
            throw new RuntimeException();
        }
        if(user.getUserRole() == 3) {
            throw new RuntimeException(); // 탈퇴 유저
        }

        String role = "";
        if(user.getUserRole() == 1) {
            role = "ROLE_USER";
        } else if(user.getUserRole() == 2) {
            role = "ROLE_OWNER";
        }

        MyUser myUser = MyUser.builder()
                .userPk(user.getUserPk())
                .role(role)
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        return SignInRes.builder()
                .userPk(user.getUserPk())
                .userName(user.getUserName())
                .userPic(user.getUserPic())
                .accessToken(accessToken).build();
    }

    @Override
    public UserInfoGetRes getUserInfo(UserInfoGetReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        //프론트에서 직접 pk 전달 X, 백엔드에서 토큰을 가지고 알아서 처리
        return mapper.selProfileUserInfo(p);
    }

    @Override
    public String patchProfilePic(UserPicPatchReq p) {
        return null;
    }

    @Override
    public Map getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
        if(cookie == null) {
            throw new RuntimeException();
        }
        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)) {
            throw new RuntimeException();
        }

        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        MyUser myUser = ((MyUserDetails)auth).getMyUser();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        Map map = new HashMap();
        map.put("accessToken", accessToken);
        return map;
    }
}
