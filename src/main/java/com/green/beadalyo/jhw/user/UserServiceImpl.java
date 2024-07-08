package com.green.beadalyo.jhw.user;

import com.green.beadalyo.jhw.common.AppProperties;
import com.green.beadalyo.jhw.common.CookieUtils;
import com.green.beadalyo.jhw.common.CustomFileUtils;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import com.green.beadalyo.jhw.user.exception.*;
import com.green.beadalyo.jhw.user.model.*;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
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
@Transactional
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
    public int postSignUp(MultipartFile pic, UserSignUpPostReq p) {
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
    public SignInRes postSignIn(HttpServletResponse res, SignInPostReq p) {
        User user = mapper.getUserById(p.getUserId());
        if(user == null || user.getUserState() == 3) {
            throw new RuntimeException("존재하지 않는 ID");
        }
        if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new RuntimeException("비밀번호 불일치");
        }
        UserAddrGetRes mainAddr = mapper.getMainAddr(user.getUserPk());

        MyUser myUser = MyUser.builder()
                .userPk(user.getUserPk())
                .role(user.getUserRole())
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        return SignInRes.builder()
                .userPk(user.getUserPk())
                .userNickname(user.getUserName())
                .userPic(user.getUserPic())
                .mainAddr(mainAddr)
                .accessToken(accessToken).build();
    }

    @Override
    public UserInfoGetRes getUserInfo() {
        long userPk = authenticationFacade.getLoginUserPk();
        //프론트에서 직접 pk 전달 X, 백엔드에서 토큰을 가지고 알아서 처리
        UserInfoGetRes result = mapper.selProfileUserInfo(userPk);
        result.setMainAddr(mapper.getMainAddr(userPk));
        return result;

    }

    @Override
    @Transactional
    public String patchProfilePic(MultipartFile pic, UserPicPatchReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        String fileName = customFileUtils.makeRandomFileName(pic);
        p.setPicName(fileName);
        mapper.updProfilePic(p);

        try {
            String midPath = String.format("user/%d", p.getSignedUserPk());
            String delAbsoluteFolderPath = String.format("%s/%s", customFileUtils.uploadPath, midPath);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);
            customFileUtils.makeFolders(midPath);
            String target = String.format("%s/%s", midPath, fileName);
            customFileUtils.transferTo(pic, target);
        } catch(Exception e) {
            throw new FileUploadFailedException();
        }
        return fileName;
    }

    @Override
    public int patchUserInfo(UserInfoPatchReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        return mapper.updUserInfo(p);
    }

    @Override
    public int patchUserPassword(UserPasswordPatchReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }else if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        }
        String hashedPassword = passwordEncoder.encode(p.getNewPw());
        p.setNewPw(hashedPassword);
        return mapper.updUserPassword(p);
    }

    @Override
    public int deleteUser(UserDelReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }else if(!BCrypt.checkpw(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        }
        return mapper.deleteUser(p.getSignedUserPk());
    }

    @Override
    public Map getAccessToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
        if(cookie == null) {
            throw new NullCookieException();
        }
        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        
        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        MyUser myUser = ((MyUserDetails)auth).getMyUser();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        Map map = new HashMap();
        map.put("accessToken", accessToken);
        return map;
    }
}
