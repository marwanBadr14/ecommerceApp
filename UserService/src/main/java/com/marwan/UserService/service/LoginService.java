package com.marwan.UserService.service;

import com.marwan.UserService.exceptions.WrongCredentialException;
import com.marwan.UserService.helper.UserMapConvertor;
import com.marwan.UserService.repository.UserRepository;
import com.marwan.UserService.reqres.AuthenticationRequest;
import com.marwan.UserService.reqres.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager manager;

    private final UserMapConvertor userMapConvertor;

    @Value("${WRONG_CREDENTIAL_EXCEPTION_MESSAGE}")
    private String WRONG_CREDENTIAL_EXCEPTION_MESSAGE;


    public Object authenticate(AuthenticationRequest request) throws IllegalAccessException, RuntimeException {

        try{

            if(!isLoginRequestValid(request)){
                throw new WrongCredentialException(
                        WRONG_CREDENTIAL_EXCEPTION_MESSAGE
                );
            }

            // retrieves the user from the database or returns an error if not found
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();

            // create a map to generate JWT token
            Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

            // generates an encoded JWT token for the created user with extra claims
            var jwtToken = jwtService.generateToken(claims, user.getUsername());


            // return the JWT token
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .role(user.getRole().toString().toLowerCase())
                    .id(user.getId())
                    .build();
        }
        catch (WrongCredentialException wrongCredentialException){
            log.error("USER ENTERED A WRONG SET OF CREDENTIALS", wrongCredentialException);
            return new ResponseEntity<String>(wrongCredentialException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

    private boolean isLoginRequestValid(AuthenticationRequest request) {
        // authenticates that username and password from HTTP request body found in DB

        return manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ).isAuthenticated();

    }


}