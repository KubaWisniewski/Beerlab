package com.app.controller;

import com.app.model.Beer;
import com.app.model.Order;
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

    @GetMapping
    public Order getOrderInprogress(@CurrentUser CustomUserDetails customUserDetails) {
        return orderService.getActualOrder(customUserDetails.getId());
    }
    @PostMapping
    public Order addBeerToOrder(@CurrentUser CustomUserDetails customUserDetails, @RequestBody Beer beer){
        System.out.println(beer);
        return orderService.addBeerToOrder(customUserDetails.getId(),beer);
    }
}
