package com.marwan.UserService.controller;

import com.marwan.UserService.reqres.AuthenticationRequest;
import com.marwan.UserService.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user  ")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // expose endpoint for "login" functionality
    // returns a JWT token

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws IllegalAccessException {
        return ResponseEntity.ok(loginService.authenticate(request));
    }
}

