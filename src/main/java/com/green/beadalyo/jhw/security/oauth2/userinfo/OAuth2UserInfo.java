package com.green.beadalyo.jhw.security.oauth2.userinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {
    //                 <key타입, value타입>
    // Social 플랫폼에서 받아온 Data(JSON)을 HashMap으로 컨버팅하여 내가 직접 DI해준다.
    protected final Map<String, Object> attributes;

    public abstract String getId(); //유니크값 리턴
    public abstract String getName(); //이름
    public abstract String getEmail(); //이메일
    public abstract String getProfilePicUrl(); //프로필 사진   https://..... or null
}
