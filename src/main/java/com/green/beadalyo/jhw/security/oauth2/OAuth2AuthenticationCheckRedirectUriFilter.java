package com.green.beadalyo.jhw.security.oauth2;

import com.green.beadalyo.common.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationCheckRedirectUriFilter extends OncePerRequestFilter {
    private final AppProperties appProperties;      // 허용한 리다이렉트 주소값을 yaml에서 정의 했다 (authorized-redirect-uris)

    @Override
    protected void doFilterInternal(HttpServletRequest request  // 요청에 대한 모든 정보
            , HttpServletResponse response  // 응답할 수 있는 객체
            , FilterChain filterChain)  // 다음 필터로 req, res를 전달할 때 사용
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();    // 호스트값 제외한 uri를 리턴 (ex. http://localhost:8080/aaa/bbb 여기서 "/aaa/bbb" 를 리턴
        log.info("requestUri : {}", requestUri);

        if (requestUri.startsWith(appProperties.getOauth2().getBaseUri())) {
            String redirectUriParam = request.getParameter("redirect_uri");
            if (redirectUriParam != null && !(hasAuthorizedRedirectUri(redirectUriParam))) {    //  허용하지 않은 URI 라면
                String errRedirectUrl = UriComponentsBuilder.fromUriString("/err_message")
                        .queryParam("message", "유효한 Redirect URL이 아닙니다.").encode()
                        .toUriString();
                //"/err_message?message=유효한 Redirect URL이 아닙니다";
                response.sendRedirect(errRedirectUrl);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    // 우리가 설정한 redirect-uri 인지 체크
    private boolean hasAuthorizedRedirectUri (String uri) {
        URI clientRedirectUri = URI.create(uri);
        log.info("clientRedirectUri.getHost() : {}", clientRedirectUri.getHost());
        log.info("clientRedirectUri.getPort() : {}", clientRedirectUri.getPort());

        for (String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris()) {
            URI authorizedUri = URI.create(redirectUri);
            if (clientRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())
                    && clientRedirectUri.getPort() == authorizedUri.getPort()
                    && clientRedirectUri.getPath().equals(authorizedUri.getPath())
            ) {
                return true;
            }
        }
        return false;
    }
}
