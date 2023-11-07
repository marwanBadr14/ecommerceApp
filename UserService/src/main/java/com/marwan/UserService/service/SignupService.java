package com.marwan.UserService.service;

import com.marwan.UserService.helper.UserMapConvertor;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import com.marwan.UserService.reqres.AuthenticationResponse;
import com.marwan.UserService.reqres.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private UserMapConvertor userMapConvertor = new UserMapConvertor();

    public Object register(RegisterRequest request) throws IllegalAccessException {

        // builds a User object using HTTP request body
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // adds user to database
        userRepository.save(user);

        // create a map to generate JWT token
        Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

        // generates an encoded JWT token for the created user with extra claims
        var jwtToken = jwtService.generateToken(claims, user.getUsername());

        // generates an encoded JWT token for the created user with no extra claims
        //var jwtToken = jwtService.generateToken(user);

        // returns the JWT token
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
