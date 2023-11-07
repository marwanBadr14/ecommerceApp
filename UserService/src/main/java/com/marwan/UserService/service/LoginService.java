package com.marwan.UserService.service;

import com.marwan.UserService.helper.UserMapConvertor;
import com.marwan.UserService.repository.UserRepository;
import com.marwan.UserService.reqres.AuthenticationRequest;
import com.marwan.UserService.reqres.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager manager;

    private final UserMapConvertor userMapConvertor = new UserMapConvertor();



    public Object authenticate(AuthenticationRequest request) throws IllegalAccessException {

        // authenticates that username and password from HTTP request body found in DB
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // retrieves the user from the database or returns an error if not found
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // create a map to generate JWT token
        Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

        // generates an encoded JWT token for the created user with extra claims
        var jwtToken = jwtService.generateToken(claims, user.getUsername());

        // generates an encoded JWT token for the authenticated user
        // var jwtToken = jwtService.generateToken(user);

        // return the JWT token
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


}
