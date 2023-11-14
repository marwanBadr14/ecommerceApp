package com.marwan.UserService.mapper;

import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.repository.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO userEntityToDto(User user){
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        return userDTO;
    }

    public User userDtoToEntity(UserDTO userDTO){
        User user = User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
        return user;
    }
}