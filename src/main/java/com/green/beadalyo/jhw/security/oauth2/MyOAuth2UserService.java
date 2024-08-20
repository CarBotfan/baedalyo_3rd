package com.green.beadalyo.jhw.security.oauth2;

import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.MyUserOAuth2Vo;
import com.green.beadalyo.jhw.security.SignInProviderType;
import com.green.beadalyo.jhw.security.oauth2.userinfo.OAuth2UserInfo;
import com.green.beadalyo.jhw.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.SignInPostReq;
import com.green.beadalyo.jhw.user.model.UserSignUpPostReq;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository repository;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest); //제공자로부터 사용자정보를 얻음
        //각 소셜플랫폼에 맞는 enum타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType.valueOf(userRequest.getClientRegistration()
                .getRegistrationId()
                .toUpperCase()
        );

        //규격화된 UserInfo객체로 변환
        // oAuth2User.getAttributes() > Data가 HashMap 객체로 변환
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes());

        //기존에 회원가입이 되어있는가 체크
        SignInPostReq signInParam = new SignInPostReq();
        signInParam.setUserId(oAuth2UserInfo.getId()); //플랫폼에서 넘어오는 유니크값(항상 같은 값이며 다른 사용자와 구별되는 유니크 값)
        signInParam.setUserLoginType(signInProviderType.getValue());

        User user;
        Optional<User> optionalUser = repository.findUserByUserId(signInParam.getUserId());
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else { //회원가입 처리
            UserSignUpPostReq signUpParam = new UserSignUpPostReq();
            signUpParam.setUserLoginType(signInProviderType.getValue());
            signUpParam.setUserId(oAuth2UserInfo.getId());
            signUpParam.setUserNickname(oAuth2UserInfo.getName());
            signUpParam.setUserPic(oAuth2UserInfo.getProfilePicUrl());
            signUpParam.setUserEmail(oAuth2UserInfo.getEmail());
            signUpParam.setUserRole("ROLE_USER");
            user = new User(signUpParam);
            user = repository.save(user);
        }

        boolean needsAdditionalInfo = user.getUserNickname() == null || user.getUserPhone() == null;
        MyUserOAuth2Vo myUserOAuth2Vo = new MyUserOAuth2Vo(user.getUserPk(), "ROLE_USER", user.getUserNickname(), user.getUserPic(), user.getUserEmail());

        MyUserDetails signInUser = new MyUserDetails();
        signInUser.setMyUser(myUserOAuth2Vo);
        signInUser.setNeedsAdditionalInfo(needsAdditionalInfo);
        return signInUser;
    }

}