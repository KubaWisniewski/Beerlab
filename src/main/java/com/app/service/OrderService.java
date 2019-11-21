package com.app.service;

import com.app.model.Beer;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.OrderStatus;
import com.app.model.dto.OrderDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.AddBeerToOrderPayload;
import com.app.payloads.requests.ChangeOrderStatusPayload;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
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
        return orderRepository.findAll().stream().sorted(Comparator.comparing(Order::getStartedTime)).map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getQueueOrders() {
        return orderRepository.findAll().stream().filter(order -> order.getStatus() != OrderStatus.CLOSED && order.getStatus() != OrderStatus.COMPLETED).sorted(Comparator.comparing(order -> order.getStartedTime())).map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getAllUserOrders(Long id) {
        return orderRepository.findByUserId(id).stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public int getUserQueuePosition(Long orderId) {
        List<OrderDto> allOrdersInQueue = getQueueOrders();
        Order order = orderRepository.findById(orderId).orElseThrow(NullPointerException::new);
        int counter = 0;
        for (OrderDto o : allOrdersInQueue) {
            if (o.getId() != order.getId()) {
                counter += 1;
            } else {
                return counter;
            }
        }
        return counter;
    }

    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id).map(modelMapper::fromOrderToOrderDto).orElseThrow(NullPointerException::new);
    }

    public OrderDto order(Long id, AddBeerToOrderPayload addBeerToOrderPayload) {
        if (beerRepository.findById(addBeerToOrderPayload.getBeerId()).get().getQuantity() == 0)
            throw new NullPointerException();
        if (!orderRepository.findByUserIdAndStatus(id, OrderStatus.NOT_PAID).isPresent())
            createEmptyOrder(id);
        Order order = orderRepository.findByUserIdAndStatus(id, OrderStatus.NOT_PAID).orElseThrow(NullPointerException::new);
        if (!order.getOrderItems().isEmpty()
                && order.getOrderItems().stream().anyMatch(x -> x.getBeer().getId().equals(addBeerToOrderPayload.getBeerId()))) {
            OrderItem orderItem = order.getOrderItems().stream().filter(x -> x.getBeer().getId().equals(addBeerToOrderPayload.getBeerId())).findFirst().get();
            orderItem.setQuantity(orderItem.getQuantity() + addBeerToOrderPayload.getQuantity());
            Beer beer = beerRepository.findById(addBeerToOrderPayload.getBeerId()).get();
            beer.setQuantity(beer.getQuantity() - addBeerToOrderPayload.getQuantity());
            order.setTotalPrice(order.getOrderItems().stream().mapToDouble(value -> value.getUnitPrice() * value.getQuantity()).sum());
            beerRepository.save(beer);
            orderRepository.save(order);
            return modelMapper.fromOrderToOrderDto(order);
        }
        Beer beer = beerRepository.findById(addBeerToOrderPayload.getBeerId()).get();
        OrderItem orderItem = OrderItem.builder().quantity(addBeerToOrderPayload.getQuantity()).unitPrice(beer.getPrice()).beer(beer).order(order).build();
        order.getOrderItems().add(orderItem);
        order.setTotalPrice(order.getOrderItems().stream().mapToDouble(value -> value.getUnitPrice() * value.getQuantity()).sum());
        beerRepository.save(beer);
        orderRepository.save(order);
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

    private void createEmptyOrder(Long userId) {
        orderRepository.save(Order
                .builder()
                .status(OrderStatus.NOT_PAID)
                .startedTime(LocalDateTime.now())
                .orderItems(new LinkedList<>())
                .user(userRepository.findById(userId).orElseThrow(NullPointerException::new))
                .totalPrice(0.00)
                .build());
    }
}
