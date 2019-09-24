package com.app.controller;

import com.app.model.Beer;
import com.app.model.Order;
import com.app.security.CurrentUser;
import com.app.security.UserPrincipal;
import com.app.service.OrderService;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Order getOrderInprogress(@CurrentUser UserPrincipal userPrincipal, HttpSession session) {
        return orderService.getActualOrder(userPrincipal.getId());
    }
    @PostMapping
    public Order addBeerToOrder(@CurrentUser UserPrincipal userPrincipal, Beer beer){
        return orderService.addBeerToOrder(userPrincipal.getId(),beer);
    }
}
