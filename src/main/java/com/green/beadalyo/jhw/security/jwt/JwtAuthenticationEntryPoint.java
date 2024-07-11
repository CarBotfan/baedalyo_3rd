package com.green.beadalyo.jhw.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정 추가
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResultError responseData = ResultError.builder().statusCode(-999).resultMsg("토큰 정보가 존재하지 않습니다.").build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseData);

        response.getWriter().write(jsonResponse);

    }
}
