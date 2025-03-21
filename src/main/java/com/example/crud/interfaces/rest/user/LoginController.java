package com.example.crud.interfaces.rest.user;

import com.example.crud.application.dto.user.LoginRequestDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.app_service.sessioncheck.SessionAndCookieCheckingService;
import com.example.crud.application.app_service.user.UserLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final SessionAndCookieCheckingService sessionAndCookieCheckingService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDto dto,
            HttpServletRequest req,
            HttpServletResponse res){
        Long userId = userLoginService.loginUser(dto);
        if(userId != null) {
            sessionAndCookieCheckingService.remember(req, res, userId);

            return ResponseEntity.ok("로그인 완료");
        }else{
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest req,
            HttpServletResponse res){
        if(req.getSession().getAttribute("userId") == null){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
        sessionAndCookieCheckingService.delete(req, res);
        return ResponseEntity.ok("로그아웃 완료");
    }
}
