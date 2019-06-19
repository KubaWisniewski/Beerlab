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
@Table(name = "orderrrrr")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    /*
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<Beer> beers = new HashSet<>();
*/
}
