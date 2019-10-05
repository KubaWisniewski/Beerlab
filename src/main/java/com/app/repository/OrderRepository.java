package com.app.repository;

import com.app.model.Order;
import com.app.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByUserIdAndStatus(Long userId, OrderStatus orderStatus);

}
