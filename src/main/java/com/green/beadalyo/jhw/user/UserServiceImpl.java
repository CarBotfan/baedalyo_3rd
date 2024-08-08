package com.green.beadalyo.jhw.user;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
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
    private final UserRepository userRepository;

    @Override
    @Transactional
    public long postUserSignUp(User user) throws Exception{
        try {
            repository.saveAndFlush(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return user.getUserPk();
    }

    @Transactional
    public void save(User user)
    {
        userRepository.save(user) ;
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
    public SignInRes postSignIn(HttpServletResponse res, User user) throws Exception{
        UserAddrGetRes addrGetRes = null;
        if(userAddrRepository.existsById(user.getUserPk())) {
            addrGetRes = new UserAddrGetRes(userAddrRepository.findMainUserAddr(user.getUserPk()));
        }
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

    @Transactional
    public int patchUserInfo(UserInfoPatchDto dto) {
        if(repository.existsByUserPhone(dto.getUserPhone())) {
            throw new DuplicatedInfoException("전화번호");
        }
        if(repository.existsByUserNickname(dto.getUserNickname())) {
            throw new DuplicatedInfoException("닉네임");
        }
        User user = repository.getReferenceById(authenticationFacade.getLoginUserPk());

        user.update(dto);
        repository.save(user);
        return 1;
    }




    @Override
    @Transactional
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
            p.setNewPw(passwordEncoder.encode(p.getNewPw()));
            user.setUserPw(p.getNewPw());
            result = 1;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public User clearUser(User user) throws Exception{
        if(user.getUserState() == 3) {
            throw new UserNotFoundException();
        }
        user.setUserPw(null);
        user.setUserName(null);
        user.setUserNickname(null);
        user.setUserPhone(null);
        user.setUserEmail(null);
        user.setUserRole(null);
        user.setUserLoginType(null);
        user.setUserPic(null);
        user.setCreatedAt(null);
        user.setUserState(3);
        return user;
    }

    public boolean checkPassword(String password1, String password2) {
        return !passwordEncoder.matches(password1, password2);
    }

    public User getUser(Long userPk) {
        if(!repository.existsById(userPk)) {
            throw new UserNotFoundException();
        }
        return repository.getReferenceById(userPk);
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

    public UserGetRes getUserGetResByPk() {
        User user = repository.findByUserPk(authenticationFacade.getLoginUserPk());
        return new UserGetRes(user);

    }

    public User getUserById(String userId)  {
        return repository.findUserByUserId(userId).orElseThrow();
    }

    @Override
    public int duplicatedIdCheck(String userId) {
        if(repository.existsByUserId(userId)) {
            throw new DuplicatedIdException();
        }
        return 1;
    }

    public void duplicatedInfoCheck(User user) {
        if(repository.existsByUserId(user.getUserId()))
        {
            throw new DuplicatedIdException();
        }
        if(repository.existsByUserEmail(user.getUserEmail())) {
            throw new DuplicatedInfoException("이메일");
        }
        if(repository.existsByUserNickname(user.getUserNickname())) {
            throw new DuplicatedInfoException("닉네임");
        }
        if(repository.existsByUserPhone(user.getUserPhone())) {
            throw new DuplicatedInfoException("전화번호");
        }
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

    @Override
    public User getUserByUserNameAndUserEmail(FindUserIdReq req) throws Exception {
        return repository.findByUserEmailAndUserName(req.getUserEmail(), req.getUserName());
    }

    @Override
    public User getUserByUserNameAndUserEmailAndUserId(FindUserPwReq req) throws Exception {
        return repository.findByUserEmailAndUserNameAndUserId(req.getUserEmail(), req.getUserName(), req.getUserId());
    }

    public Boolean confirmPw(FindUserPwReq req) {
        if (req.getUserPw().equals(req.getUserPwConfirm())) {
            return true;
        } else return false;
    }

    public Integer saveUser(User user) {
        try {
            repository.save(user);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public User putUserEssential(PutSocialLoginReq req) {
        User user = getUser(authenticationFacade.getLoginUserPk());
        user.setUserNickname(req.getUserNickName());
        user.setUserPhone(req.getUserPhone());

        return user;
    }

}

