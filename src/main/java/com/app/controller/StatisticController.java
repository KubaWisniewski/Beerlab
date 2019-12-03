package com.app.controller;

import com.app.model.dto.ReportDto;
import com.app.repository.ReportRepository;
import com.app.service.StatisticService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    private StatisticService statisticService;

    public StatisticController(StatisticService statisticService, ReportRepository reportRepository) {
        this.statisticService = statisticService;
    }

    @GetMapping("/all")
    public List<ReportDto> getAllReports() {
        return statisticService.getAllReports();
    }

    @GetMapping("/{id}")
    public ReportDto getReportById(@PathVariable Long id) {
        return statisticService.getReportById(id);
    }

    @GetMapping("/realizationOrderTime")
    public long realizationTime() {
        return statisticService.realizationOrderTime();
    }

    @GetMapping("/newReport")
    public ReportDto generateNewReport() {
        return statisticService.createNewReport();
    }

    @GetMapping
    public ReportDto getLastReport() {
        return statisticService.getLastReport();
    }

    @PostMapping
    public void testGenerateData() {
        statisticService.testupdateReportData();
    }

}
