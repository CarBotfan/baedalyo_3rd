package com.green.beadalyo.jhw.security.oauth2;

import com.green.beadalyo.common.AppProperties;
import com.green.beadalyo.common.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("OAuth2AuthenticationFailureHandler - onAuthenticationFailure");
        System.out.println(request);
        exception.printStackTrace();

        String targetUrl = cookieUtils.getCookie(request
                , appProperties.getOauth2().getRedirectUriParamCookieName()
                , String.class
        );

        repository.removeAuthorizationRequestCookies(response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
