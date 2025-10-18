package com.example.demo.service;

import java.util.List;

import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(UserMapper.userToUserDto(null)).toList;
    }

    public UserDTO getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException(User with "id" + Long id "not found"))
        return UserMapper.userToUserDto(user);
    }
}
