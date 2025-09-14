package com.sagarw.project.uber.uberApp.controllers;

import com.sagarw.project.uber.uberApp.dto.SignupDto;
import com.sagarw.project.uber.uberApp.dto.UserDto;
import com.sagarw.project.uber.uberApp.services.AuthService;
import com.sagarw.project.uber.uberApp.services.Impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    UserDto signUp(@RequestBody SignupDto signupDto){
        return authService.signup(signupDto);
    }
}
