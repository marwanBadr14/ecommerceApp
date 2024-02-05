package com.marwan.UserService.service;


import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.exceptions.AdminCreationException;
import com.marwan.UserService.exceptions.UserNotFoundException;
import com.marwan.UserService.mapper.UserMapper;
import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Value("${ADMIN_CREATION_EXCEPTION_MESSAGE}")
    private String ADMIN_CREATION_EXCEPTION_MESSAGE;

    @Value("${USER_NOT_FOUND_EXCEPTION_MESSAGE}")
    private String USER_NOT_FOUND_EXCEPTION_MESSAGE;


    public ResponseEntity<String> addAdmin(String adminFirstName, String adminLastName, String adminEmail, String adminPassword)
            throws RuntimeException{
        try{
            if(adminFirstName.isBlank() || adminLastName.isBlank() || adminEmail.isBlank() || adminPassword.isBlank()){
                throw new AdminCreationException(ADMIN_CREATION_EXCEPTION_MESSAGE);
            }
            User admin = User.builder()
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            return new ResponseEntity<String>("Admin was added successfully", HttpStatus.CREATED);
        }
        catch (Exception adminCreationException){
            log.error("ERROR WHILE CRATING ADMIN", adminCreationException);
            return new ResponseEntity<String>(adminCreationException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public String getUserEmailById(Integer id){
        User user = userRepository.getReferenceById(id);
        UserDTO userDTO = userMapper.userEntityToDto(user);
        return userDTO.getEmail();
    }

    public void deleteAdmin(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        User admin = user.orElseThrow();
        userRepository.delete(admin);
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.userEntityToDto(users);
    }

    public ResponseEntity getUserById(Integer id) throws RuntimeException{
        try {
            User user = userRepository.getReferenceById(id);
            if(user == null){
                throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE, id);
            }
            UserDTO userDTO = userMapper.userEntityToDto(user);
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }
        catch (UserNotFoundException userNotFoundException){
            log.error("NO USER WITH THIS ID WAS FOUND", userNotFoundException);
            return new ResponseEntity<String>(userNotFoundException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<UserDTO> getAllAdmins() {
        List<User> admins = userRepository.findByRole(Role.ADMIN);

        return userMapper.userEntityToDto(admins);
    }
}
