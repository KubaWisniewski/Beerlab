package com.app.service;

import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Double getUserBalance(Long id) {
        return userRepository.getOne(id).getBalance();
    }
}