package com.app.controller;

import com.app.payloads.requests.LoginPayload;
import com.app.payloads.requests.RegisterPayload;
import com.app.payloads.responses.ApiPayload;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@Valid @RequestBody LoginPayload loginPayload) {
        userService.authenticateUser(loginPayload.getEmail(),loginPayload.getPassword());
        return ResponseEntity.ok().body(new ApiPayload(true, "User logged in successfully"));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterPayload registerPayload) {
        userService.signUp(registerPayload);
        return ResponseEntity.ok().body(new ApiPayload(true, "User registered successfully"));
    }
}
