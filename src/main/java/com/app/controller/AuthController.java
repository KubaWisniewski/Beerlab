package com.app.controller;

import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.payloads.requests.LoginPayload;
import com.app.payloads.requests.RegisterPayload;
import com.app.payloads.responses.ApiPayload;
import com.app.payloads.responses.JwtAuthenticationPayload;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;    
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok().body("Hello world");
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser( @Valid @RequestBody LoginPayload loginPayload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginPayload.getEmail(),
                        loginPayload.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok().body(JwtAuthenticationPayload.builder().accessToken(jwt).tokenType("Bearer").build());
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterPayload registerPayload) {
        if (userRepository.findByEmail(registerPayload.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ApiPayload(false, "Email Address already in use!"));
        }

        User user = User.builder().username(registerPayload.getUsername()).email(
                registerPayload.getEmail()).password(registerPayload.getPassword()).build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER);
        user.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(user);

        return ResponseEntity.ok().body(new ApiPayload(true, "User registered successfully"));
    }
}