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
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "group")
    private User groupOwner;
    private Set<User> members = new HashSet<>();

}
