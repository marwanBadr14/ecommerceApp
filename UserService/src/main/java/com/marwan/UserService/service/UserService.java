package com.marwan.UserService.service;


import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.mapper.UserMapper;
import com.marwan.UserService.repository.Role;
import com.marwan.UserService.repository.User;
import com.marwan.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        User admin = User.builder()
                    .firstName(user.get().getFirstName())
                    .lastName(user.get().getLastName())
                    .email(user.get().getEmail())
                    .password(user.get().getPassword())
                    .id(user.get().getId())
                    .role(user.get().getRole())
                    .build();
        userRepository.delete(admin);
    }
}
