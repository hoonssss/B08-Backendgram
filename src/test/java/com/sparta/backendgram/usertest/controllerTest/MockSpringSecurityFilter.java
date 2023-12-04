package com.sparta.backendgram.usertest.controllerTest;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class MockSpringSecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication != null) {
            System.out.println("MockSpringSecurityFilter: 인증 정보 설정 - " + authentication.getName());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
        System.out.println("MockSpringSecurityFilter: 인증 정보 해제");
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
