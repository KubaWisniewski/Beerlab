package com.app.model.dto;

import com.app.model.Beer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime start = LocalDateTime.now();
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime end;
    private int users;
    private int orders;
    private Double startBeersValue;
    private List<Double> avgBeerPrice = new ArrayList<>();
    private Double potentialIncome;
    private List<Beer> beers = new LinkedList<>();
    private List<String> mostPopularBeers = new LinkedList<>();
}
