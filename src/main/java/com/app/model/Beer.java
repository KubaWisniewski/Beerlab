package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Beer {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String imgUrl;
    private String brand;
    private Double price;
    private Integer quantity;
    /*
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    */
}
