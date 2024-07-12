package com.green.beadalyo.jhw.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.beadalyo.gyb.common.ResultError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // 인코딩 설정 추가
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ResultError responseData = ResultError.builder().statusCode(-998).resultMsg("필요한 권한이 존재하지 않습니다..").build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseData);

        response.getWriter().write(jsonResponse);    }
}
