package com.app.service;

import com.app.model.Beer;
import com.app.model.Order;
import com.app.model.OrderItem;
import com.app.model.OrderStatus;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private BeerRepository beerRepository;

    public OrderService(OrderRepository orderRepository, BeerRepository beerRepository) {
        this.orderRepository = orderRepository;
        this.beerRepository = beerRepository;
    }

    public Order getActualOrder(Long userId) {
        Order order = orderRepository.findByUserIdAndStatus(userId,OrderStatus.INPROGRESS);
        if (order != null) {
            return order;
        } else
            return null;
    }

    public Order addBeerToOrder(Long userId, Beer beer) {

        Order order = getActualOrder(userId);
        if (order == null) {
            createOrder(userId);
        }
        Optional<Beer> beerTmp = beerRepository.findById(beer.getId());
        OrderItem orderItem = OrderItem.builder().order(order).beer(beerTmp.get()).build();
        order.getOrderItems().add(orderItem);
        orderRepository.save(order);
        return order;
    }

    public Order createOrder(Long userId) {
        Order order = new Order();
        order.setStatus(OrderStatus.INPROGRESS);
        orderRepository.save(order);
        return order;
    }


}
