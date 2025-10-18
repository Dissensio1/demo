package com.example.demo.mapper;

import java.util.stream.Collectors;

import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserLoggedDTO;
import com.example.demo.model.Permission;
import com.example.demo.model.User;

public class UserMapper {
    
    public static UserDTO userToUserDto(User user){
        return new UserDTO(user.getId(), 
        user.getUsername(), 
        user.getPassword(), 
        user.getRole().getAuthority(), 
        user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }

    public static UserLoggedDTO userToUserLoggedDto(User user){
        return new UserLoggedDTO( user.getUsername(), 
        user.getRole().getAuthority(), 
        user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }
}
