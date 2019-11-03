package com.app.controller;


import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.payloads.requests.ChangeOrderStatusPayload;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import com.app.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Api(value = "Order API")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(
            value = "Add beer to order",
            response = OrderDto.class
    )
    @PostMapping
    public OrderDto createOrder(@CurrentUser CustomUserDetails customUserDetails, @RequestBody BeerDto beerDto) {
        return orderService.createOrder(customUserDetails.getId(), beerDto);
    }

    @ApiOperation(
            value = "Fetch all orders",
            response = OrderDto.class
    )
    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @ApiOperation(
            value = "Get one order",
            response = OrderDto.class
    )
    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/{id}")
    public OrderDto changeOrderStatus(@PathVariable Long id, @RequestBody ChangeOrderStatusPayload changeOrderStatusPayload){
        return orderService.changeOrderStatus(id,changeOrderStatusPayload);
    }
}
