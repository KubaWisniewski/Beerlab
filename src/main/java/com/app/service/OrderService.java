package com.app.service;

import com.app.exception.NotEnoughBalanceException;
import com.app.model.*;
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
        return orderRepository.findAll().stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getQueueOrders() {
        return orderRepository.findAll().stream().filter(order -> order.getStatus() != OrderStatus.CLOSED && order.getStatus() != OrderStatus.NOT_PAID).sorted(Comparator.comparing(Order::getStartedTime)).map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> getAllUserOrders(Long id) {
        return orderRepository.findByUserId(id).stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public int getUserQueuePosition(Long orderId) {
        List<OrderDto> allOrdersInQueue = getQueueOrders();
        Order order = orderRepository.findById(orderId).orElseThrow(NullPointerException::new);
        int counter = 0;
        for (OrderDto o : allOrdersInQueue) {
            if (!o.getId().equals(order.getId())) {
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

    public OrderDto getNotPaidUserOrder(Long id) {
        return orderRepository.findByUserIdAndStatus(id, OrderStatus.NOT_PAID).map(modelMapper::fromOrderToOrderDto).isPresent() ? orderRepository.findByUserIdAndStatus(id, OrderStatus.NOT_PAID).map(modelMapper::fromOrderToOrderDto).get() : createEmptyOrder(id);
    }

    public OrderDto deleteBeerFromOrder(Long orderId, Long beerId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NullPointerException::new);

        OrderItem orderItem = order.getOrderItems().stream().filter(x -> x.getBeer().getId().equals(beerId)).findFirst().get();

        Beer beer = beerRepository.findById(beerId).get();
        beer.setQuantity(beer.getQuantity() + orderItem.getQuantity());

        order.setOrderItems(order.getOrderItems().stream().filter(y -> !y.getBeer().getId().equals(beerId)).collect(Collectors.toList()));
        order.setTotalPrice(order.getOrderItems().stream().mapToDouble(value -> value.getUnitPrice() * value.getQuantity()).sum());

        orderItem.setOrder(null);

        beerRepository.save(beer);
        orderRepository.save(order);


        return modelMapper.fromOrderToOrderDto(order);
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
        beer.setQuantity(beer.getQuantity() - addBeerToOrderPayload.getQuantity());
        OrderItem orderItem = OrderItem.builder().quantity(addBeerToOrderPayload.getQuantity()).unitPrice(beer.getPrice()).beer(beer).order(order).build();
        order.getOrderItems().add(orderItem);
        order.setTotalPrice(order.getOrderItems().stream().mapToDouble(value -> value.getUnitPrice() * value.getQuantity()).sum());
        beerRepository.save(beer);
        orderRepository.save(order);
        return modelMapper.fromOrderToOrderDto(order);
    }

    public OrderDto reduceQuantity(Long id, AddBeerToOrderPayload addBeerToOrderPayload) {

        Order order = orderRepository.findById(id).orElseThrow(NullPointerException::new);

        OrderItem orderItem = order.getOrderItems().stream().filter(x -> x.getBeer().getId().equals(addBeerToOrderPayload.getBeerId())).findFirst().get();
        orderItem.setQuantity(orderItem.getQuantity() - 1);

        Beer beer = beerRepository.findById(addBeerToOrderPayload.getBeerId()).get();
        beer.setQuantity(beer.getQuantity() + 1);

        order.setTotalPrice(order.getOrderItems().stream().mapToDouble(value -> value.getUnitPrice() * value.getQuantity()).sum());

        beerRepository.save(beer);
        orderRepository.save(order);


        return modelMapper.fromOrderToOrderDto(order);
    }

    public OrderDto confirmOrder(Long id, Long method) {
        Order order = orderRepository.findByUserIdAndStatus(id, OrderStatus.NOT_PAID).orElseThrow(NullPointerException::new);
        if (method == 1L) {
            User user = userRepository.findById(id).orElseThrow(NullPointerException::new);

            if (user.getBalance() < order.getTotalPrice()) {
                throw new NotEnoughBalanceException();
            }
            user.setBalance(user.getBalance() - order.getTotalPrice());
            order.setStatus(OrderStatus.QUEUED);
            userRepository.save(user);
        } else if (method == 2L) {
            order.setStatus(OrderStatus.CASH_PAID);
        }
        order.setStartedTime(LocalDateTime.now());

        orderRepository.save(order);
        return modelMapper.fromOrderToOrderDto(order);
    }

    public List<OrderDto> getUserCompletedOrders(Long userId) {
        return orderRepository.findAllByUserIdAndStatus(userId, OrderStatus.COMPLETED).stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
    }

    public OrderDto changeOrderStatus(Long id, ChangeOrderStatusPayload changeOrderStatusPayload) {
        Order order = orderRepository.findById(id).orElseThrow(NullPointerException::new);
        if (changeOrderStatusPayload.getOrderStatus().equals(OrderStatus.COMPLETED.toString())) {
            order.setCompleteTime(LocalDateTime.now());
        }
        order.setStatus(OrderStatus.valueOf(changeOrderStatusPayload.getOrderStatus()));
        orderRepository.save(order);
        return modelMapper.fromOrderToOrderDto(order);
    }

    private OrderDto createEmptyOrder(Long userId) {
        return modelMapper.fromOrderToOrderDto(orderRepository.save(Order
                .builder()
                .status(OrderStatus.NOT_PAID)
                .orderItems(new LinkedList<>())
                .user(userRepository.findById(userId).orElseThrow(NullPointerException::new))
                .totalPrice(0.00)
                .build()));
    }
}
