package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private final RestaurantService ResService;


    @Override
    @Transactional
    public long postSignUp(MultipartFile pic, UserSignUpPostReq p) throws Exception{
        if(mapper.getUserById(p.getUserId()) != null) {
            throw new DuplicatedIdException();
        }
        if(p.getUserPw() != p.getUserPwConfirm()) {
            throw new PwConfirmFailureException();
        }
        String fileName = customFileUtils.makeRandomFileName(pic);
        p.setUserPic(fileName);
        String password = passwordEncoder.encode(p.getUserPw());
        p.setUserPw(password);
        mapper.signUpUser(p);
        long result = p.getUserPk();
        if(pic == null) {
            return result;
        }
        try {
            String path = String.format("user/%d", p.getUserPk());
            customFileUtils.makeFolder(path);
            String target = String.format("%s/%s", path, fileName);
            customFileUtils.transferTo(pic, target);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadFailedException();
        }
        return result;
    }

    @Override
    public SignInRes postSignIn(HttpServletResponse res, SignInPostReq p) throws Exception{
        User user = mapper.getUserById(p.getUserId());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
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
    public UserInfoGetRes getUserInfo() throws Exception{
        long userPk = authenticationFacade.getLoginUserPk();
        UserInfoGetRes result = mapper.selProfileUserInfo(userPk);
        if(result == null) {
            throw new UserNotFoundException();
        }
        result.setMainAddr(mapper.getMainAddr(userPk));
        return result;

    }

    @Override
    @Transactional
    public String patchProfilePic(MultipartFile pic, UserPicPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        String fileName = customFileUtils.makeRandomFileName(pic);
        p.setPicName("user/" + fileName);
        int result = mapper.updProfilePic(p);
        if(result != 1) {
            throw new UserPatchFailureException();
        }
        try {
            String delAbsoluteFolderPath = String.format("%s", customFileUtils.uploadPath);
            File file = new File(delAbsoluteFolderPath, fileName);
            file.delete();
            String target = String.format("%s",fileName);
            customFileUtils.transferTo(pic, target);
        } catch(Exception e) {
            throw new FileUploadFailedException();
        }
        return fileName;
    }

    @Override
    public int patchUserNickname(UserNicknamePatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        int result = mapper.updUserNickname(p);
        if(result != 1) {
            throw new UserPatchFailureException();
        }
        return result;
    }

    @Override
    public int patchUserPhone(UserPhonePatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        int result = mapper.updUserPhone(p);
        if(result != 1) {
            throw new UserPatchFailureException();
        }
        return result;
    }

    @Override
    public int patchUserPassword(UserPasswordPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        } else if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        } else if(p.getNewPw() != p.getNewPwConfirm()) {
            throw new PwConfirmFailureException();
        }
        String hashedPassword = passwordEncoder.encode(p.getNewPw());
        p.setNewPw(hashedPassword);
        return mapper.updUserPassword(p);
    }

    @Override
    public int deleteUser(UserDelReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }else if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        }
        return mapper.deleteUser(p.getSignedUserPk());
    }

    @Override
    public int deleteOwner(UserDelReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user = mapper.getUserByPk(p.getSignedUserPk());
        if(user == null || user.getUserState() == 3) {
            throw new UserNotFoundException();
        }else if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        }
        int result1 = mapper.deleteUser(p.getSignedUserPk());
        try { ResService.deleteRestaurantData(p.getSignedUserPk()); }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result1;
    }

    @Override
    public Map getAccessToken(HttpServletRequest req) throws Exception{
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

    @Override
    public User getUserByPk(long signedUserPk) {
        User user = mapper.getUserByPk(signedUserPk);
        return user;
    }

    @Override
    public User getUserByPk() {
        long signedUserPk = authenticationFacade.getLoginUserPk();
        User user = mapper.getUserByPk(signedUserPk);
        return user;
    }
}
