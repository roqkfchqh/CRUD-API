package com.example.crud.infrastructure.filter;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.service.UserService;
import com.example.crud.domain.user_root.aggregate.User;
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
        User sessionUser = (User) req.getSession().getAttribute("user");
        if(sessionUser != null){
            chain.doFilter(request, response);
            return;
        }

        //비로그인 사용자일 경우 nickname, password 요청
        if (req.getRequestURI().startsWith("/api/posts") || req.getRequestURI().startsWith("/api/comments")) {
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            if(nickname == null || nickname.isEmpty() || password == null || password.isEmpty()){
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }
        }

        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("rememberMe".equals(cookie.getName())){
                    String email = cookie.getValue();
                    User user = Objects.requireNonNull(userService).getUserByEmail(email);
                    if(user != null){
                        req.getSession().setAttribute("user", user);
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
