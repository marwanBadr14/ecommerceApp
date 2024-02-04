package com.marwan.UserService.service;

import com.marwan.UserService.exceptions.EmailAlreadyExistsException;
import com.marwan.UserService.helper.UserMapConvertor;
import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import com.marwan.UserService.reqres.AuthenticationResponse;
import com.marwan.UserService.reqres.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserMapConvertor userMapConvertor;

    private static final String EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE = "An account with this email already exists";

    public Object register(RegisterRequest request) throws IllegalAccessException, RuntimeException {

        try {
            if(!isRegisterRequestValid(request)){
                throw new EmailAlreadyExistsException(
                        EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE, request.getEmail()
                );
            }

            // builds a User object using HTTP request body
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.CUSTOMER)
                    .build();

            // adds user to database
            userRepository.save(user);

            // create a map to generate JWT token
            Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

            // generates an encoded JWT token for the created user with extra claims
            var jwtToken = jwtService.generateToken(claims, user.getUsername());


            // returns the JWT token
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .role(user.getRole().toString())
                    .build();
        }
        catch (EmailAlreadyExistsException emailAlreadyExistsException){
            log.error("ERROR OCCURRED DURING USER REGISTRATION", emailAlreadyExistsException);
            return new ResponseEntity<String>(emailAlreadyExistsException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isRegisterRequestValid(RegisterRequest request) {
        return userRepository.findByEmail(request.getEmail()).isEmpty();
    }
}