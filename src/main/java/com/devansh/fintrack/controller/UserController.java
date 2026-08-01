package com.devansh.fintrack.controller;

import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.service.UserService;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);

        return ResponseEntity.ok(user);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User updateUser){
        User updatedUser = userService.updateUser(id, updateUser);

        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deletUserById(@PathVariable Long id){
        User deleteUserById = userService.deleteUser(id);
        return ResponseEntity.ok(deleteUserById);
    }
}
