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
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.*;
import com.green.beadalyo.jhw.user.model.*;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.jhw.useraddr.UserAddrServiceImpl;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import com.green.beadalyo.jhw.useraddr.repository.UserAddrRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    private final RestaurantService resService;
    private final UserRepository repository;
    private final UserAddrRepository userAddrRepository;
    private final UserAddrServiceImpl userAddrService;

    private static int IMAGE_SIZE_LIMIT = 3145728;

    @Override
    @Transactional
    public long postUserSignUp(User user) throws Exception{
        repository.save(user);
        return user.getUserPk();
    }


    @Transactional
    public String uploadProfileImage(MultipartFile pic) {
        if(pic == null) {
            return null;
        }
        if (!Objects.requireNonNull(pic.getContentType()).startsWith("image/")) {
            throw new InvalidRegexException();
        }
        if(pic.getSize() > IMAGE_SIZE_LIMIT) {
            throw new RuntimeException("파일은 3MB 이하여야 합니다.");
        }
        customFileUtils.makeFolder("user");
        String fileName = String.format("user/%s", customFileUtils.makeRandomFileName(pic));

        try {
            customFileUtils.transferTo(pic, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadFailedException();
        }
        return fileName;
    }

    @Transactional
    public void deleteProfileImage() {
        User user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        try {
            String delAbsoluteFolderPath = String.format("%s", customFileUtils.uploadPath);
            File file = new File(delAbsoluteFolderPath, user.getUserPic());
            file.delete();
        } catch (Exception e) {
            throw new FileUploadFailedException();
        }
    }

    @Override
    @Transactional
    public int postOwnerSignUp(MultipartFile pic, OwnerSignUpPostReq p) {
//        try {
//            UserSignUpPostReq req = new UserSignUpPostReq(p);
//            long userPk = postSignUp(req);
//            RestaurantInsertDto dto = new RestaurantInsertDto();
//            dto.setUser(userPk);
//            dto.setName(p.getRestaurantName());
//            dto.setRegiNum(p.getRegiNum());
//            dto.setResAddr(p.getAddr());
//            dto.setDesc1(p.getDesc1());
//            dto.setDesc2(p.getDesc2());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//            dto.setOpenTime(LocalTime.parse(p.getOpenTime(), formatter));
//            dto.setCloseTime(LocalTime.parse(p.getCloseTime(), formatter));
//            dto.setResCoorX(p.getCoorX());
//            dto.setResCoorY(p.getCoorY());
//            resService.insertRestaurantData(dto);
//        } catch(RuntimeException e){
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return 1;
    }

    @Override
    public SignInRes postSignIn(HttpServletResponse res, User user) throws Exception{
        UserAddrGetRes addrGetRes = new UserAddrGetRes(userAddrRepository.findMainUserAddr(user.getUserPk()));
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
                .mainAddr(addrGetRes)
                .userPhone(user.getUserPhone())
                .userRole(user.getUserRole())
                .accessToken(accessToken)
                .tokenMaxAge(LocalDateTime.now().plusSeconds((long)(0.001 * appProperties.getJwt().getAccessTokenExpiry()))).build();
    }

    @Override
    public UserInfoGetRes getUserInfo(User user) throws Exception{
        long userPk = authenticationFacade.getLoginUserPk();
        if(user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        return new UserInfoGetRes(user);

    }

    @Override
    @Transactional
    public String patchProfilePic(MultipartFile pic) throws Exception{
        return null;
    }

    public int patchUserInfo(UserInfoPatchDto dto) {
        User user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        user.update(dto);
        repository.save(user);
        return 1;
    }

    @Override
    public int patchUserNickname(UserNicknamePatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user;
        try {
            user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        } catch(EntityNotFoundException e) {
            throw new UserNotFoundException();
        }
        if(user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        int result = 0;
        try {
            user.setUserNickname(p.getUserNickname());
            result = 1;
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = handleSQLException((SQLIntegrityConstraintViolationException) cause);
                throw new DuplicatedInfoException(errorMessage);
            } else {
                // 기타 예외 처리
                throw new RuntimeException(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public int patchUserPhone(UserPhonePatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        User user;
        try {
            user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        } catch(EntityNotFoundException e) {
            throw new UserNotFoundException();
        }
        int result = 0;
        try {
            user.setUserPhone(p.getUserPhone());
            result = 1;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String errorMessage = handleSQLException((SQLIntegrityConstraintViolationException) cause);
                throw new DuplicatedInfoException(errorMessage);
            } else {
                // 기타 예외 처리

                throw new RuntimeException(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public int patchUserPassword(UserPasswordPatchReq p) throws Exception{
        User user;
        int result = 0;
        try {
            user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        } catch(EntityNotFoundException e) {
            throw new UserNotFoundException();
        }
        if(user.getUserState() == 3) {
            throw new UserNotFoundException();
        } else if(!passwordEncoder.matches(p.getUserPw(), user.getUserPw())) {
            throw new IncorrectPwException();
        } else if(!p.getNewPw().equals(p.getNewPwConfirm())) {
            throw new PwConfirmFailureException();
        }
        try {
            user.setUserPw(p.getNewPw());
            result = 1;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteUser(User user) throws Exception{
        if(user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        user.setUserState(3);
        return 1;
    }

    public boolean checkPassword(String password1, String password2) {
        return !passwordEncoder.matches(password1, password2);
    }

    public User getUser(Long userPk) {
        try {
        return repository.getReferenceById(userPk);
        } catch(EntityNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public int deleteOwner(User user) {
        user.setUserState(3);
        return 1;
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

    public UserGetRes getUserByPk() {
        User user = repository.getReferenceById(authenticationFacade.getLoginUserPk());
        return new UserGetRes(user);

    }

    public User getUserById(String userId)  {
        try {
            return repository.findUserByUserId(userId);
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public int duplicatedCheck(String userId) {
        if(userId.length() < 8) {
            throw new RuntimeException("Id는 8자 이상이어야 합니다.");
        }
        User user = repository.findUserByUserId(userId);
        UserInfoGetRes result = new UserInfoGetRes(user);
        if(result.getUserId().equals(userId)) {
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
    private String handleSQLException(SQLException sqlEx) {
        String sqlState = sqlEx.getSQLState();
        int errorCode = sqlEx.getErrorCode();
        String message = sqlEx.getMessage();
        String key = extractDuplicatedKey(message);
        if (key != null) {
            if(key.equals("user_nickname")) {
                return "닉네임";
            } else if(key.equals("user_phone")) {
                return "전화번호";
            }
        }
        return key;
    }

    private String extractDuplicatedKey(String message) {
        // MySQL 패턴
        Pattern mysqlPattern = Pattern.compile("for key '(.+?)'");
        Matcher mysqlMatcher = mysqlPattern.matcher(message);
        if (mysqlMatcher.find()) {
            return mysqlMatcher.group(1);
        }
        return null;
    }
}

