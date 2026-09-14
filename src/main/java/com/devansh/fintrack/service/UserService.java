package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.request.CreateUserRequestDto;
import com.devansh.fintrack.dto.request.UpdateUserRequestDto;
import com.devansh.fintrack.dto.response.UserResponseDto;
import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.exception.DuplicateResourceException;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(CreateUserRequestDto request){
        User user = mapToEntity(request);


        if(emailExists(user)){
            throw new DuplicateResourceException(
                    "User with email " + user.getEmail() + " already exists");
        }
        User savedUser = userRepository.save(user);

        return mapToDto(savedUser);
    }

    public UserResponseDto getUser(Long id){
          User user = userRepository.findById(id)
                  .orElseThrow(() ->
                          new ResourceNotFoundException("User with id " + id + " not found"));
          return mapToDto(user);
    }

    public List<UserResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .toList();

    }

    public UserResponseDto updateUser(Long id, UpdateUserRequestDto updateUser){

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"));

        if (userRepository.existsByEmailAndIdNot(
                updateUser.getEmail(), id)) {

            throw new DuplicateResourceException(
                    "User with email " + updateUser.getEmail()
                            + " already exists");
        }

        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(updateUser.getPassword());

        User savedUser = userRepository.save(existingUser);

        return mapToDto(savedUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

         userRepository.delete(user);

    }

    private User mapToEntity(CreateUserRequestDto request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    private UserResponseDto mapToDto(User user){
        UserResponseDto response = new UserResponseDto();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
    private boolean emailExists(User user){
        return userRepository.existsByEmail(user.getEmail());
    }
}
