package com.marwan.UserService.mapper;

import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.repository.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDTO userEntityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public List<UserDTO> userEntityToDto(List<User> users){
        return users.stream()
                .map(this::userEntityToDto)
                .toList();
    }

    public User userDtoToEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }
}