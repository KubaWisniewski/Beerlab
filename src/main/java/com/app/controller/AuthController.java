package com.app.controller;

import com.app.payloads.requests.LoginPayload;
import com.app.payloads.requests.RegisterPayload;
import com.app.payloads.responses.ApiPayload;
import com.app.security.CustomUserDetails;
import com.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "Authentication controller")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(
            value = "Sign in",
            response = ResponseEntity.class
    )
    @PostMapping("/signin")
    public ResponseEntity signIn(@Valid @RequestBody LoginPayload loginPayload) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userService.authenticateUser(loginPayload.getEmail(), loginPayload.getPassword()).getPrincipal();
        return ResponseEntity.ok().body(userService.getUserInformation(customUserDetails.getId()));
    }

    @ApiOperation(
            value = "Sign up",
            response = ResponseEntity.class
    )
    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterPayload registerPayload) {
        userService.signUp(registerPayload);
        return ResponseEntity.ok().body(new ApiPayload(true, "User registered successfully"));
    }
}
