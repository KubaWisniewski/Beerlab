package com.app.service;

import com.app.exception.UserRegisterException;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.model.dto.UserDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.CreateWorkerAccountPayload;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    public AdminService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    private Boolean checkEmailExists(String email) {
        return userRepository.existsUserByEmail(email).orElseThrow(UserRegisterException::new);
    }

    public void createWorkerAccount(CreateWorkerAccountPayload createWorkerAccountPayload) {
        if (checkEmailExists(createWorkerAccountPayload.getEmail()))
            throw new UserRegisterException();
        User user = User.builder().username(createWorkerAccountPayload.getUsername()).email(
                createWorkerAccountPayload.getEmail()).password(createWorkerAccountPayload.getPassword()).build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.valueOf(createWorkerAccountPayload.getRole())).orElseThrow(NullPointerException::new);
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(modelMapper::fromUserToUserDto).collect(Collectors.toList());
    }
}
