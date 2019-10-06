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

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private RoleRepository roleRepository;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginPayload loginPayload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginPayload.getEmail(),
                        loginPayload.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
//                header("Authorization", "Bearer " + jwt).body(JwtAuthenticationPayload.builder().accessToken(jwt).tokenType("Bearer").user(userRepository.findByEmail(loginPayload.getEmail()).get()).build());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody RegisterPayload registerPayload) {
        if (userRepository.findByEmail(registerPayload.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiPayload(false, "Email Address already in use!"));
        }

        User user = User.builder().username(registerPayload.getUsername()).email(
                registerPayload.getEmail()).password(registerPayload.getPassword()).build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER);
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        return ResponseEntity.ok().body(new ApiPayload(true, "User registered successfully"));
    }
}
