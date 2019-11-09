package com.app.repository;

import com.app.model.Order;
import com.app.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
     List<Order> findByUserId(Long userId);

     Optional<Order> findByUserIdAndStatus(Long id, OrderStatus status);
}
