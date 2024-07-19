package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.SignInProviderType;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import com.green.beadalyo.jhw.user.exception.*;
import com.green.beadalyo.jhw.user.model.*;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    private static int IMAGE_SIZE_LIMIT = 3145728;

    @Override
    @Transactional
    public long postSignUp(MultipartFile pic, UserSignUpPostReq p) throws Exception{

        p.setUserLoginType(SignInProviderType.LOCAL.getValue());
        long result;
        if(mapper.getUserById(p.getUserId()) != null) {
            throw new DuplicatedIdException();
        }
        if(!p.getUserPw().equals(p.getUserPwConfirm())) {
            throw new PwConfirmFailureException();
        }
        String password = passwordEncoder.encode(p.getUserPw());
        p.setUserPw(password);

        if(pic == null) {
            mapper.signUpUser(p);
            result = p.getUserPk();
            return result;
        } else if (!pic.getContentType().startsWith("image/")) {
            throw new InvalidRegexException();
        }
        if(pic.getSize() > IMAGE_SIZE_LIMIT) {
            throw new RuntimeException("파일은 3MB 이하여야 합니다.");
        }
        customFileUtils.makeFolder("user");
        String fileName = String.format("user/%s", customFileUtils.makeRandomFileName(pic));
        p.setUserPic(fileName);
        mapper.signUpUser(p);
        result = p.getUserPk();

        try {
            customFileUtils.transferTo(pic, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadFailedException();
        }
        return result;
    }

    @Override
    @Transactional
    public int postOwnerSignUp(MultipartFile pic, OwnerSignUpPostReq p) {
        try {
            UserSignUpPostReq req = UserSignUpPostReq.builder()
                    .userId(p.getUserId())
                    .userPw(p.getUserPw())
                    .userPwConfirm(p.getUserPwConfirm())
                    .userName(p.getUserName())
                    .userNickname(p.getUserNickname())
                    .userPhone(p.getUserPhone())
                    .userEmail(p.getUserEmail())
                    .userRole("ROLE_OWNER")
                    .build();
            long userPk = postSignUp(pic, req);
            RestaurantInsertDto dto = new RestaurantInsertDto();
            dto.setUser(userPk);
            dto.setName(p.getRestaurantName());
            dto.setRegiNum(p.getRegiNum());
            dto.setResAddr(p.getAddr());
            dto.setDesc1(p.getDesc1());
            dto.setDesc2(p.getDesc2());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            dto.setOpenTime(LocalTime.parse(p.getOpenTime(), formatter));
            dto.setCloseTime(LocalTime.parse(p.getCloseTime(), formatter));
            dto.setResCoorX(p.getCoorX());
            dto.setResCoorY(p.getCoorY());
            resService.insertRestaurantData(dto);
        } catch(RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Override
    public SignInRes postSignIn(HttpServletResponse res, SignInPostReq p) throws Exception{
        p.setUserLoginType(SignInProviderType.LOCAL.getValue());
        User user = mapper.signInUser(p);
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
                .userNickname(user.getUserNickname())
                .mainAddr(mainAddr)
                .userPhone(user.getUserPhone())
                .userRole(user.getUserRole())
                .accessToken(accessToken)
                .tokenMaxAge(LocalDateTime.now().plusSeconds((long)(0.001 * appProperties.getJwt().getAccessTokenExpiry()))).build();
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
        String originalFileName = mapper.getUserPicName(p.getSignedUserPk());
        if(originalFileName != null) {
            try {
                String delAbsoluteFolderPath = String.format("%s", customFileUtils.uploadPath);
                File file = new File(delAbsoluteFolderPath, originalFileName);
                file.delete();
            } catch (Exception e) {
                throw new FileUploadFailedException();
            }
        }
        String fileName = "user/" + customFileUtils.makeRandomFileName(pic);
        if(pic != null) {
            if(pic.getSize() > IMAGE_SIZE_LIMIT) {
                throw new RuntimeException("파일은 3MB 이하여야 합니다.");
            }
            p.setPicName(fileName);
        }
        int result = mapper.updProfilePic(p);
        if(result != 1) {
            throw new UserPatchFailureException();
        }
        if(pic == null) {
            return null;
        } else if (!pic.getContentType().startsWith("image/")) {
            throw new InvalidRegexException();
        }
        try {
            customFileUtils.makeFolder("user");
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
        map.put("TokenMaxAge", LocalDateTime.now().plusSeconds((long)(0.001 * appProperties.getJwt().getAccessTokenExpiry())));
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
        if(userId.length() < 8) {
            throw new RuntimeException("Id는 8자 이상이어야 합니다.");
        }
        User user = mapper.getUserById(userId);
        if(user != null) {
            throw new DuplicatedIdException();
        }
        return 1;
    }

    public void logoutToken(HttpServletRequest request, HttpServletResponse response) {

//        String token = jwtTokenProvider.resolveToken(request);

//        Base64.Decoder decoder = Base64.getDecoder();
//        final String[] splitJwt = Objects.requireNonNull(token).split("\\.");
//        final String payloadStr = new String(decoder.decode(splitJwt[1].replace('-', '+' )
//                .replace('_', '/' ).getBytes()));
        //base 64의 경우 "-"와 "_"가 없기 때문에 illegal base64 character 5f 에러 발생
//        Long userId = getUserIdFromToken(payloadStr);
//        Date expirationDate = getDateExpFromToken(payloadStr);
        cookieUtils.deleteCookie(response, "refresh-token");
    }
//
//    public Long getUserIdFromToken(String payloadStr) {
//        JsonObject jsonObject = new Gson().fromJson(payloadStr, JsonObject.class);
//        return jsonObject.get("id").getAsLong();
//    }
//
//    public Date getDateExpFromToken(String payloadStr) {
//        JsonObject jsonObject = new Gson().fromJson(payloadStr, JsonObject.class);
//        long exp = jsonObject.get("exp").getAsLong();
//        return new Date(exp * 1000);
//    }



}
