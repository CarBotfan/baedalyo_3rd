package com.green.beadalyo.jhw.security.oauth2.userinfo;

import java.util.Map;


public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    /*
        부모가 기본생성자가 아닌 생성자(오버라이딩 생성자)만 가지고 있는 경우는
        lombok애노테이션으로 처리 불가
        그래서, 직접 생성자 작성해야 함
     */
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String)attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getProfilePicUrl() {
        return (String)attributes.get("picture");
    }
}
