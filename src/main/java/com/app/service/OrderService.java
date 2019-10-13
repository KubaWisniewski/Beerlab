package com.app.service;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Beer;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.OrderStatus;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private BeerRepository beerRepository;
    private UserRepository userRepository;
    public OrderService(OrderRepository orderRepository, BeerRepository beerRepository, UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
    }

    public Order getActualOrder(Long userId) {
        Order order = orderRepository.findByUserIdAndStatus(userId,OrderStatus.INPROGRESS).get();
        if (order != null) {
            return order;
        } else
            return null;
    }

    public Order addBeerToOrder(Long userId, Beer beer) {

        Order order = getActualOrder(userId);
        if (order == null) {
            order = createOrder(userId);
        }
        Beer beerTmp = beerRepository.findById(beer.getId()).orElseThrow(ResourceNotFoundException::new);
        OrderItem orderItem = OrderItem.builder().order(order).beer(beerTmp).build();
        order.getOrderItems().add(orderItem);
        beerTmp.getOrderItems().add(orderItem);
        orderRepository.save(order);
        beerRepository.save(beerTmp);
        return order;
    }

    public Order createOrder(Long userId) {
        Order order = new Order();
        order.setUser(userRepository.findById(userId).get());
        order.setStatus(OrderStatus.INPROGRESS);
        orderRepository.save(order);
        return order;
    }


}
