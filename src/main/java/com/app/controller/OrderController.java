package com.app.controller;

import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import com.app.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDto addBeerToOrder(@CurrentUser CustomUserDetails customUserDetails, @RequestBody BeerDto beerDto) {
        return orderService.createOrder(customUserDetails.getId(), beerDto);
    }
}
