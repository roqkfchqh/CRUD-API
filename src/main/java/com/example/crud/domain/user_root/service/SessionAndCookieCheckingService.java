package com.example.crud.domain.user_root.service;

import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class SessionAndCookieCheckingService {

    public void remember(HttpServletRequest req, HttpServletResponse res, User user) {
        req.getSession().setAttribute("user", user);
        req.getSession().setMaxInactiveInterval(1800);

        Cookie rememberMeCookie = new Cookie("rememberMe", user.getEmail());
        rememberMeCookie.setSecure(true);
        rememberMeCookie.setHttpOnly(true);
        rememberMeCookie.setMaxAge(7 * 24 * 60 * 60);   //7일
        rememberMeCookie.setPath("/");
        res.addCookie(rememberMeCookie);
    }

    public void delete(HttpServletRequest req, HttpServletResponse res) {
        req.getSession().invalidate();
        Cookie rememberMeCookie = new Cookie("rememberMe", null);
        rememberMeCookie.setMaxAge(0);
        rememberMeCookie.setPath("/");
        res.addCookie(rememberMeCookie);
    }
}
