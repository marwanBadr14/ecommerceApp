package com.marwan.UserService.service;


import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public void addAdmin(String adminFirstName, String adminLastName, String adminEmail, String adminPassword) {
        User admin = User.builder()
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .email(adminEmail)
                .password(adminPassword)
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
