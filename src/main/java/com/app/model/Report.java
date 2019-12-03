package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime start = LocalDateTime.now();
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime end;
    private int users;
    private int orders;
    @ElementCollection
    @CollectionTable(name = "report_avgBeerPrices", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "avgBeerPrice")
    private List<Double> avgBeerPrice = new ArrayList<>();
}
