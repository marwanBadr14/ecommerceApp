package com.marwan.UserService.service;


import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.mapper.UserMapper;
import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public void addAdmin(String adminFirstName, String adminLastName, String adminEmail, String adminPassword) {
        User admin = User.builder()
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }

    public String getUserEmailById(Integer id) {
        User user = userRepository.getReferenceById(id);

        UserDTO userDTO = userMapper.userEntityToDto(user);

        return userDTO.getEmail();
    }

    public void deleteAdmin(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        User admin = user.orElseThrow();
        userRepository.delete(admin);
    }

    public void promoteUser(String email) {
        Optional<User> userOp = userRepository.findByEmail(email);
        User user = userOp.orElseThrow();
        User admin = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .id(user.getId())
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.userEntityToDto(users);
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.getReferenceById(id);
        return userMapper.userEntityToDto(user);
    }

    public void demoteUser(String email) {
        Optional<User> userOp = userRepository.findByEmail(email);
        User user = userOp.orElseThrow();
        User customer = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .id(user.getId())
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(customer);
    }
}
