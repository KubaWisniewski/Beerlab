package com.app.service;

import com.app.exception.UserRegisterException;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.model.dto.UserDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.RegisterPayload;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public Double getUserBalance(Long id) {
        return userRepository.getOne(id).getBalance();
    }

    public UserDto getUserInformation(Long id) {
        return userRepository.findById(id).map(modelMapper::fromUserToUserDto).orElseThrow(NullPointerException::new);
    }

    private Boolean checkEmailExists(String email) {
        return userRepository.existsUserByEmail(email).orElseThrow(UserRegisterException::new);
    }

    public void authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void signUp(RegisterPayload registerPayload) {
        if (checkEmailExists(registerPayload.getEmail()))
            throw new UserRegisterException();
        User user = User.builder().username(registerPayload.getUsername()).email(
                registerPayload.getEmail()).password(registerPayload.getPassword()).build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(NullPointerException::new);
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }
}
