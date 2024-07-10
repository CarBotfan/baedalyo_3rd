package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.SignInProviderType;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final RestaurantService resService;

    private String regexPhone = "^\\d{3}-\\d{4}-\\d{4}$";
    String regexFileName = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.(jpg|jpeg|png|gif|bmp|tiff|svg)$";

    Pattern phonePattern = Pattern.compile(regexPhone);
    Pattern filePattern = Pattern.compile(regexFileName);


    @Override
    @Transactional
    public long postSignUp(MultipartFile pic, UserSignUpPostReq p) throws Exception{
        p.setUserLoginType(SignInProviderType.LOCAL.getValue());
        if(mapper.getUserById(p.getUserId()) != null) {
            throw new DuplicatedIdException();
        }
        if(!p.getUserPw().equals(p.getUserPwConfirm())) {
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
        User user = mapper.signInUser(p);
        p.setUserLoginType(SignInProviderType.LOCAL.getValue());
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
                .userNickname(user.getUserName())
                .mainAddr(mainAddr)
                .userRole(user.getUserRole())
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
        Matcher matcher = filePattern.matcher(pic.getOriginalFilename());
        if(!matcher.matches()) {
            throw new InvalidRegexException();
        }
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
        Matcher matcher = phonePattern.matcher(p.getUserPhone());
        if(!matcher.matches()) {
            throw new InvalidRegexException();
        }
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
        } else if(!p.getNewPw().equals(p.getNewPwConfirm())) {
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
        try { resService.deleteRestaurantData(p.getSignedUserPk()); }
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
        return mapper.getUserByPk(signedUserPk);

    }

    @Override
    public User getUserByPk() {
        long signedUserPk = authenticationFacade.getLoginUserPk();
        User user = mapper.getUserByPk(signedUserPk);
        return user;
    }

    @Override
    public int duplicatedCheck(String userId) {
        User user = mapper.getUserById(userId);
        if(user != null) {
            throw new DuplicatedIdException();
        }
        return 1;
    }
}
