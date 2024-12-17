package com.example.crud.interfaces.rest.user;

import com.example.crud.application.dto.user.CurrentPasswordRequestDto;
import com.example.crud.application.dto.user.UpdateRequestDto;
import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.application.app_service.session.SessionAndCookieCheckingService;
import com.example.crud.application.app_service.user.UserService;
import com.example.crud.application.app_service.validation.UserValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final SessionAndCookieCheckingService sessionAndCookieCheckingService;

    //read
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(
            HttpServletRequest req){
        User sessionUser = getSessionUser(req);
        return ResponseEntity.ok(userService.readUser(sessionUser));
    }

    //update
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UpdateRequestDto dto,
            HttpServletRequest req){
        User sessionUser = getSessionUser(req);
        UserResponseDto user = userService.updateUser(dto, sessionUser);
        return ResponseEntity.ok(user);
    }

    //delete
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(
            @RequestBody CurrentPasswordRequestDto currentPasswordRequestDto,
            HttpServletRequest req,
            HttpServletResponse res){
        User sessionUser = getSessionUser(req);
        userService.deleteUser(sessionUser, currentPasswordRequestDto);
        req.getSession().invalidate();
        sessionAndCookieCheckingService.delete(req, res);
        return ResponseEntity.ok("회원 탈퇴가 정상적으로 완료되었습니다.");
    }

    private User getSessionUser(HttpServletRequest req) {
        User sessionUser = (User) req.getSession().getAttribute("user");
        userValidationService.validateUser(sessionUser);
        return sessionUser;
    }
}
