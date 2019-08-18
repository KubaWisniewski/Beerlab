package com.app.controller;

import com.app.security.CurrentUser;
import com.app.security.UserPrincipal;
import com.app.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public String getUserInformation(@ApiIgnore @CurrentUser UserPrincipal userDetails) {
        return userDetails.getUsername();
    }

    @GetMapping("/balance")
    public Double getUserBalance(@ApiIgnore @CurrentUser UserPrincipal userDetails) {
        return userService.getUserBalance(userDetails.getId());
    }
}
