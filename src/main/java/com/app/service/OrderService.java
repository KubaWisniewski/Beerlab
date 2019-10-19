package com.app.service;

import com.app.model.*;
import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;


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

    public OrderDto createOrder(Long userId, BeerDto beerDto) {
        Order order = new Order();
        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        order.setUser(user);
        order.setStatus(OrderStatus.INPROGRESS);
        Beer beerTmp = beerRepository.findById(beerDto.getId()).orElseThrow(NullPointerException::new);
        beerTmp.setQuantity(beerTmp.getQuantity() - 1);
        OrderItem orderItem = OrderItem.builder().order(order).beer(beerTmp).build();
        order.getOrderItems().add(orderItem);
        beerTmp.getOrderItems().add(orderItem);
        user.setBalance(user.getBalance() - beerTmp.getPrice());
        beerRepository.save(beerTmp);
        orderRepository.save(order);
        return modelMapper.fromOrderToOrderDto(order);
    }

}
