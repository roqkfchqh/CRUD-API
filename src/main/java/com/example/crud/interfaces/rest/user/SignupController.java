package com.example.crud.interfaces.rest.user;

import com.example.crud.application.dto.user.SignupRequestDto;
import com.example.crud.application.app_service.session.SessionAndCookieCheckingService;
import com.example.crud.application.app_service.user.UserSignupService;
import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignupController {

    private final UserSignupService userSignupService;
    private final SessionAndCookieCheckingService sessionAndCookieCheckingService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignupRequestDto dto,
            HttpServletRequest req,
            HttpServletResponse res) {
        User user = userSignupService.registerUser(dto);

        sessionAndCookieCheckingService.remember(req, res, user);

        return ResponseEntity.ok("회원가입 완료");
    }
}
