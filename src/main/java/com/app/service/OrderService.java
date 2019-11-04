package com.app.service;

import com.app.model.*;
import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.ChangeOrderStatusPayload;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.Comparator;
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
        return orderRepository.findAll().stream().sorted(Comparator.comparing(order -> order.getStartedTime())).map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    List<OrderDto> getQueueOrders() {
        return orderRepository.findAll().stream().filter(order -> order.getStatus() != OrderStatus.CLOSED && order.getStatus() != OrderStatus.COMPLETED).sorted(Comparator.comparing(order -> order.getStartedTime())).map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getAllUserOrders(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return orderRepository.findByUserId(userDetails.getId()).stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public int getUserQueuePosition(Long userDetails) {
        List<OrderDto> allOrdersInQueue = getQueueOrders();
        User u = userRepository.findById(userDetails).orElseThrow(NullPointerException::new);
        List<Order> userOrders = u.getOrders().stream().filter(order -> order.getStatus() != OrderStatus.CLOSED && order.getStatus() != OrderStatus.COMPLETED).collect(Collectors.toList());
        int counter = 0;
        for (OrderDto o : allOrdersInQueue) {

            for (Order userOrder : userOrders) {
                if (o.getId() != userOrder.getId()) {
                    counter += 1;
                } else {
                    return counter + 1;
                }
            }
        }
        return counter;
    }

    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id).map(modelMapper::fromOrderToOrderDto).orElseThrow(NullPointerException::new);
    }

    public OrderDto createOrder(Long userId, BeerDto beerDto) {
        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        Beer beer = beerRepository.findById(beerDto.getId()).orElseThrow(NullPointerException::new);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.QUEUED);
        OrderItem orderItem = OrderItem.builder().order(order).beer(beer).build();
        order.getOrderItems().add(orderItem);
        order.setStartedTime(LocalDateTime.now());
        beer.setQuantity(beer.getQuantity() - 1);
        orderRepository.save(order);
        beerRepository.save(beer);
        userRepository.save(user);
        return modelMapper.fromOrderToOrderDto(order);
    }

    public OrderDto changeOrderStatus(Long id, ChangeOrderStatusPayload changeOrderStatusPayload) {
        Order order = orderRepository.findById(id).orElseThrow(NullPointerException::new);
        if (changeOrderStatusPayload.getOrderStatus().equals("COMPLETED")) {
            order.setCompleteTime(LocalDateTime.now());
        }
        order.setStatus(OrderStatus.valueOf(changeOrderStatusPayload.getOrderStatus()));
        orderRepository.save(order);
        return modelMapper.fromOrderToOrderDto(order);
    }
}
