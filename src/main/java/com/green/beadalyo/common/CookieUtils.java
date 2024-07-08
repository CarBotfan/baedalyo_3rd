package com.green.beadalyo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtils {
    private final ObjectMapper om;

    public <T> T getData(T type, HttpServletRequest req, String name) {
        try {
            Cookie cookie = getCookie(req, name);
            String json = cookie.getValue();
            return (T) om.readValue(json, type.getClass());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //요청 header에 내가 원하는 쿠키를 찾는 메소드
    public Cookie getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies(); //요청에서 모든 쿠키 정보를 받는다.
        if(cookies != null && cookies.length > 0) { //쿠키 정보가 있고 쿠키가 하나 이상 있다면
            for(Cookie cookie : cookies) {

                //찾고자하는 key가 있는지 확인 후 있다면 해당 쿠키 리턴
                if(name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void setCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/"); //Root URL (우리 백엔드 모든 요청에 해당하게 세팅)
        cookie.setHttpOnly(true); //보안 쿠키
        cookie.setMaxAge(maxAge); //만료 시간
        res.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse res, String name) {
        setCookie(res, name, null, 0);
    }
}

