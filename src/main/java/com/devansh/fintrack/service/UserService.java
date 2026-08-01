package com.devansh.fintrack.service;

import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.exception.DuplicateResourceException;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    public User getUser(Long id){
          return userRepository.findById(id)
                  .orElseThrow(() ->
                          new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updateUser){
        User existingUser =  userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));

        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(updateUser.getPassword());

        return userRepository.save(existingUser);
    }

    public User deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

         userRepository.delete(user);
         return user;

    }
}
