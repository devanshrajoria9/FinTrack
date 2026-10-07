package com.devansh.fintrack.controller;

import com.devansh.fintrack.dto.request.LoginRequestDto;
import com.devansh.fintrack.dto.response.LoginResponseDto;
import com.devansh.fintrack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request){
        LoginResponseDto response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
