package com.app.service;

import com.app.model.Beer;
import com.app.model.Order;
import com.app.model.Report;
import com.app.model.User;
import com.app.model.dto.BeerDto;
import com.app.model.dto.ReportDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.ReportRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private OrderRepository orderRepository;
    private BeerRepository beerRepository;
    private ReportRepository reportRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    public StatisticService(OrderRepository orderRepository, BeerRepository beerRepository, ReportRepository reportRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.beerRepository = beerRepository;
        this.reportRepository = reportRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public Long realizationOrderTime() {
        List<LocalDateTime> startedTimeOrder = orderRepository.findAll().stream().filter(order -> order.getStartedTime() != null).map(Order::getStartedTime).collect(Collectors.toList());
        List<LocalDateTime> completedTimeOrder = orderRepository.findAll().stream().filter(order -> order.getCompleteTime() != null).map(Order::getCompleteTime).collect(Collectors.toList());
        long elapsedMinutes = 0;
        for (LocalDateTime startDate : startedTimeOrder) {
            for (LocalDateTime endDate : completedTimeOrder) {
                elapsedMinutes = Duration.between(endDate, startDate).toMinutes();
            }
        }
        elapsedMinutes = elapsedMinutes / completedTimeOrder.size();
        return elapsedMinutes;
    }

    public ReportDto createNewReport() {

        Report report = reportRepository.findAll().stream().filter(report1 -> report1.getEnd() == null).findFirst().orElse(new Report());
        report = updateReportData(report);
        if(report.getStartBeersValue() == null){
            reportRepository.delete(report);
        }
        else {
            report.setPotentialIncome(calculatePotentialIncome(report));
            report.setEnd(LocalDateTime.now());
            reportRepository.saveAndFlush(report);
        }

        reportRepository.save(new Report());
        Report newReport = reportRepository.findAll().stream().filter(report1 -> report1.getEnd() == null).findFirst().orElseThrow(NullPointerException::new);
               newReport.setStart(LocalDateTime.now());
               newReport.setStartBeersValue(calculateBeersValue());

        updateReportData(newReport);
        return modelMapper.fromReportToReportDto(newReport);
    }

    public Double calculateAvgBeerPrice() {
        List<Beer> beers = beerRepository.findAll();
        double avgBeerPrice = beers.stream().map(beer -> beer.getPrice()).collect(Collectors.averagingDouble(value -> value.doubleValue()));
        return avgBeerPrice;
    }

    public Double calculateBeersValue() {
        List<Beer> beers = beerRepository.findAll();
        double BeersValue = beers.stream().map(beer -> beer.getPrice()*beer.getQuantity()).mapToDouble(value -> value.doubleValue()).sum();
        return BeersValue;
    }

    public double calculatePotentialIncome(Report report) {
        if(report.getPotentialIncome() == null){
            report.setPotentialIncome(0.0);
        }
         return (report.getStartBeersValue() - calculateBeersValue()) * 0.94;
    }

    public Report updateReportData(Report report) {
        report.getAvgBeerPrice().add(calculateAvgBeerPrice());
        report.setOrders(orderRepository.findAll().size());
        report.setUsers(userRepository.findAll().size());
        reportRepository.saveAndFlush(report);
        return report;
    }

    public ReportDto testupdateReportData() {
        Report report = reportRepository.findAll().stream().filter(report1 -> report1.getEnd() == null).findFirst().orElseThrow(NullPointerException::new);
        report.getAvgBeerPrice().add(calculateAvgBeerPrice() + getRandomNumber());
        reportRepository.saveAndFlush(report);
        return modelMapper.fromReportToReportDto(report);
    }

    public ReportDto getLastReport() {
        return modelMapper.fromReportToReportDto(reportRepository.findAll().stream().filter(report1 -> report1.getEnd() == null).findFirst().orElseThrow(NullPointerException::new));
    }

    public static double getRandomNumber() {

        return new Random().ints(1, (10 + 1)).findFirst().getAsInt();
    }

    public List<ReportDto> getAllReports() {
        return reportRepository.findAll().stream().map(modelMapper::fromReportToReportDto).filter(report -> report.getEnd() != null).sorted(Comparator.comparing(ReportDto::getStart).reversed()).collect(Collectors.toList());
    }

    public ReportDto getReportById(Long id) {
        return modelMapper.fromReportToReportDto(reportRepository.findById(id).orElseThrow(NullPointerException::new));
    }
}
