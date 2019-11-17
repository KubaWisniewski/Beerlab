package com.app.service;

import com.app.exception.UserRegisterException;
import com.app.model.Gender;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.model.dto.UserDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.domain.Transactions;
import com.app.payloads.requests.PayPalPayload;
import com.app.payloads.requests.RegisterPayload;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public void signUp(RegisterPayload registerPayload) {
        if (checkEmailExists(registerPayload.getEmail()))
            throw new UserRegisterException();
        User user = User.builder().username(registerPayload.getUsername()).email(
                registerPayload.getEmail()).password(registerPayload.getPassword()).build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(NullPointerException::new);
        user.setRoles(Collections.singletonList(userRole));
        user.setBalance(0.00);
        user.setDateOfBirth(LocalDate.parse(registerPayload.getDateOfBirth()));
        Gender gender = parseGenderFromFrontend(registerPayload);
        user.setGender(gender);
        userRepository.save(user);
    }

    public Gender parseGenderFromFrontend(RegisterPayload registerPayload) {
        Gender gender = Gender.OTHER;
        switch (registerPayload.getGender()) {
            case "Mezczyzna":
                gender = Gender.MALE;
                break;
            case "Kobieta":
                gender = Gender.FEMALE;
                break;
            case "Inna":
                gender = Gender.OTHER;
                break;
        }
        return gender;

    }

    public void addBalanceForUser(Long id, PayPalPayload payPalPayload) {
        if (id == null || payPalPayload == null) {
            throw new NullPointerException("User id or Paypal data is null");
        }
        double amountToAdd = 0;
        User user = userRepository.findById(id).orElseThrow(NullPointerException::new);
        UserDto userDto = modelMapper.fromUserToUserDto(user);
        for (Transactions p : payPalPayload.getTransactions()) {
            amountToAdd = p.getAmount().getTotal() * 2; // magic number 1$ = 2 kufle maybe in future Admin could change 1$ = x Kufle
        }
        userDto.setBalance(userDto.getBalance() + amountToAdd);
        userRepository.save(modelMapper.fromUserDtoToUser(userDto));
    }
}
