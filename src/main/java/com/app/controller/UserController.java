package com.app.controller;

import com.app.repository.UserRepository;
import com.app.security.CurrentUser;
import com.app.security.UserPrincipal;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public String getUserInformation(@ApiIgnore @CurrentUser UserPrincipal userDetails) {
        return userDetails.getUsername();
    }
}