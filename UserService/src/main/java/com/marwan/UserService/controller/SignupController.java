package com.marwan.UserService.controller;

import com.marwan.UserService.reqres.RegisterRequest;
import com.marwan.UserService.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;


    // expose endpoint for "signup" functionality
    // returns a JWT token

    @PostMapping("/signup")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) throws IllegalAccessException {
        return ResponseEntity.ok(signupService.register(request));
    }



}

