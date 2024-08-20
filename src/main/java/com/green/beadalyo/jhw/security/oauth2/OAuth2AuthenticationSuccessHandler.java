package com.green.beadalyo.jhw.security.oauth2;

import com.green.beadalyo.jhw.security.MyUser;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.MyUserOAuth2Vo;
import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(response.isCommitted()) { //응답 객체가 만료된 경우(다른곳에서 응답처리를 한 경우)
            log.error("onAuthenticationSuccess - 응답이 만료됨");
            return;
        }
        String targetUrl = determineTargetUrl(request, response, authentication); //리다이렉트 할 Url 얻음
        log.info("targetUrl: {}", targetUrl);
        clearAuthenticationAttributes(request, response); //리다이렉트 전 사용했던 자료 삭제
        getRedirectStrategy().sendRedirect(request, response, targetUrl); //리다이렉트 실행
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // FE가 소셜 로그인 시 보내준 redirect_uri 값
        String redirectUri = cookieUtils.getCookie(request, appProperties.getOauth2().getRedirectUriParamCookieName(), String.class);

        // (yaml) app.oauth2.uthorized-redirect-uris 리스트에 없는 Uri인 경우
        if (redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)) {
            throw new IllegalArgumentException("인증되지 않은 Redirect URI입니다.");
        }

        log.info("determineTargetUrl > getDefaultTargetUrl(): {}", getDefaultTargetUrl());

        // FE가 원하는 redirect_url 값이 저장
        String targetUrl = redirectUri == null ? getDefaultTargetUrl() : redirectUri;

        // MyOAuth2UserService에서 보내준 MyUserDetails를 얻는다.
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        // MyUserDetails로부터 MyUserOAuth2Vo를 얻는다.
        MyUserOAuth2Vo myUserOAuth2Vo = (MyUserOAuth2Vo) myUserDetails.getMyUser();

        // JWT를 만들기 위해 MyUser 객체화
        MyUser myUser = MyUser.builder()
                .userPk(myUserOAuth2Vo.getUserPk())
                .role(myUserOAuth2Vo.getRole())
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        // refreshToken은 보안 쿠키를 이용해서 처리 (FE가 따로 작업을 하지 않아도 아래 cookie 값은 항상 넘어온다.)
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.setCookie(response, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge);

        // 추가 정보를 요구하는 경우
        boolean needsAdditionalInfo = myUserDetails.isNeedsAdditionalInfo();

        // 리디렉션 URL에 쿼리 파라미터를 추가하여 구성
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("user_name", myUserOAuth2Vo.getNm()) // 닉네임 추가
                .queryParam("user_pic", myUserOAuth2Vo.getPic()) // 프로필 사진 추가
                .queryParam("access_token", accessToken) // 엑세스 토큰 추가
                .queryParam("needs_additional_info", needsAdditionalInfo) // 추가 정보 필요 여부 추가
                .encode()
                .toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        repository.removeAuthorizationRequestCookies(response);
    }

    //우리가 설정한 redirect-uri인지 체크
    private boolean hasAuthorizedRedirectUri(String uri) {
        URI savedCookieRedirectUri = URI.create(uri);
        log.info("savedCookieRedirectUri.getHost(): {}", savedCookieRedirectUri.getHost());
        log.info("savedCookieRedirectUri.getPort(): {}", savedCookieRedirectUri.getPort());

        for(String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris()) {
            URI authorizedUri = URI.create(redirectUri);
            if(savedCookieRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())
                    && savedCookieRedirectUri.getPort() == authorizedUri.getPort()) {
                return true;
            }
        }
        return false;
    }
}