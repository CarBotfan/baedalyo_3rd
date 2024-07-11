package com.green.beadalyo.jhw.security.oauth2.userinfo;

import com.green.beadalyo.jhw.security.SignInProviderType;
import org.springframework.stereotype.Component;

import java.util.Map;

//클래스 이름에 Factory가 들어가면 객체를 생성하는 역할
//구글, 카카오, 네이버에서 받은 JSON Data  >  HashMap  >  규격화된 객체로 변환 (데이터 가져오는 부분 통일)

@Component
public class OAuth2UserInfoFactory {
    public OAuth2UserInfo getOAuth2UserInfo(SignInProviderType signInProviderType
            , Map<String, Object> attributes) {
        return switch(signInProviderType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            default -> throw new RuntimeException("제공하지 않는 로그인 방식입니다.");
        };
    }
}
