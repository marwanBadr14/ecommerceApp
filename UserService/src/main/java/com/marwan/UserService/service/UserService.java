package com.marwan.UserService.service;


import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.mapper.UserMapper;
import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
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
}
