package com.app.controller;

import com.app.model.dto.OrderDto;
import com.app.model.dto.UserDto;
import com.app.payloads.requests.PayPalPayload;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import com.app.service.OrderService;
import com.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@Api(tags = "User controller")
public class UserController {
    private UserService userService;
    private OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @ApiOperation(
            value = "Get information about user",
            response = UserDto.class
    )
    @GetMapping("/me")
    public UserDto getUserInformation(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return userService.getUserInformation(userDetails.getId());
    }

    @ApiOperation(
            value = "Get user balance",
            response = Double.class
    )
    @GetMapping("/balance")
    public Double getUserBalance(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return userService.getUserBalance(userDetails.getId());
    }

    @ApiOperation(
            value = "Get user orders",
            response = OrderDto.class
    )
    @GetMapping("/orders")
    public List<OrderDto> getUserOrders(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return orderService.getAllUserOrders(userDetails.getId());
    }

    @ApiOperation(
            value = "Get user completed orders",
            response = OrderDto.class
    )
    @GetMapping("/completedOrders")
    public List<OrderDto> getUserCompletedOrders(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return orderService.getUserCompletedOrders(userDetails.getId());
    }

    @ApiOperation(
            value = "Confirm order",
            response = OrderDto.class
    )
    @PostMapping("/confirm/{method}")
    public OrderDto confirmOrder(@CurrentUser CustomUserDetails userDetails, @PathVariable Long method) {
        return orderService.confirmOrder(userDetails.getId(), method);
    }

    @ApiOperation(
            value = "Get not paid user order",
            response = OrderDto.class
    )
    @GetMapping("/order")
    public OrderDto getNotPaidOrder(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return orderService.getNotPaidUserOrder(userDetails.getId());
    }

    @ApiOperation(
            value = "Add money for user",
            response = ResponseEntity.class
    )
    @PostMapping("/addMoney")
    public ResponseEntity addBalanceForUser(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @RequestBody PayPalPayload payPalPayload) {
        userService.addBalanceForUser(userDetails.getId(), payPalPayload);
        return ResponseEntity.ok().body("OK");
    }

    @ApiOperation(
            value = "Set username",
            response = ResponseEntity.class
    )
    @PostMapping("/setUsername")
    public ResponseEntity setUsername(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @RequestBody String newUsername) {
        userService.setUsername(userDetails.getId(), newUsername);
        return ResponseEntity.ok().body("OK");
    }

    @ApiOperation(
            value = "Set password",
            response = ResponseEntity.class
    )
    @PostMapping("/setPassword")
    public ResponseEntity setPassword(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @RequestBody String newPassword) {
        userService.setPassword(userDetails.getId(), newPassword);
        return ResponseEntity.ok().body("OK");
    }
}

