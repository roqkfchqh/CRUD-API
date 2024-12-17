package com.example.crud.infrastructure.filter;

import com.example.crud.application.app_service.user.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.util.Objects;

@NoArgsConstructor(force = true)
public class AuthFilter implements Filter {

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //로그인 사용자인지 확인
        Long sessionUserId = (Long) req.getSession().getAttribute("userId");
        if(sessionUserId != null){
            chain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("rememberMe".equals(cookie.getName())){
                    String email = cookie.getValue();
                    Long userId = Objects.requireNonNull(userService).getUserByEmail(email).getId();
                    if(userId != null){
                        req.getSession().setAttribute("userId", userId);
                    }
                }
            }
        }

        if(req.getRequestURI().startsWith("/sign/login")){
            chain.doFilter(request, response);
            return;
        }

        res.setStatus(HttpStatusCode.valueOf(401).value());
    }
}
