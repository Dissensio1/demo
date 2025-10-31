package com.example.demo.service;

import java.util.List;

import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(UserMapper::userToUserDto).toList();
    }

    public UserDTO getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User with id " + id + " not found"));
        return UserMapper.userToUserDto(user);
    }

    public User getUser(String name){
        User user = userRepository.findByUsername(name).orElseThrow(
            () -> new ResourceNotFoundException("User with username " + name + " not found"));
        return user;
    }
}
