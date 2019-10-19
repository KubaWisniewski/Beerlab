package com.app.model.dto;

import com.app.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private Set<OrderItemDto> orderItemsDto = new HashSet<>();

    private OrderStatus status;

}
