package com.app.service;

import com.app.model.*;
import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderService {
    private OrderRepository orderRepository;
    private BeerRepository beerRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    public OrderService(OrderRepository orderRepository, BeerRepository beerRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getAllUserOrders(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return orderRepository.findByUserId(userDetails.getId()).stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id).map(modelMapper::fromOrderToOrderDto).orElseThrow(NullPointerException::new);
    }

    public OrderDto createOrder(Long userId, BeerDto beerDto) {
        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        Beer beer = beerRepository.findById(beerDto.getId()).orElseThrow(NullPointerException::new);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.INPROGRESS);
        OrderItem orderItem = OrderItem.builder().order(order).beer(beer).build();
        order.getOrderItems().add(orderItem);
        beer.setQuantity(beer.getQuantity() - 1);
        orderRepository.save(order);
        beerRepository.save(beer);
        userRepository.save(user);
        return modelMapper.fromOrderToOrderDto(order);
    }
}
