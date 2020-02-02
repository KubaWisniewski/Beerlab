package com.app.schedulers;

import com.app.model.Beer;
import com.app.model.dto.BeerDto;
import com.app.model.dto.ReportDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import com.app.repository.ReportRepository;
import com.app.service.BeerService;
import com.app.service.StatisticService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableAsync
public class StatisticsScheduler {

    private List<BeerDto> previousBeers;
    private List<BeerDto> previousBeersPopular;

    private BeerRepository beerRepository;
    private ModelMapper modelMapper;
    private BeerService beerService;
    private StatisticService statisticService;
    private ReportRepository reportRepository;

    public StatisticsScheduler(BeerRepository beerRepository, ModelMapper modelMapper, BeerService beerService, StatisticService statisticService, ReportRepository reportRepository) {
        this.beerRepository = beerRepository;
        this.modelMapper = modelMapper;
        this.beerService = beerService;
        this.statisticService = statisticService;
        this.reportRepository = reportRepository;
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void calculateBeerPrices() {

        List<BeerDto> beerlist = this.beerService.getBeers();

            if(this.previousBeers != null){

            for (int i = 0;i < beerlist.size();  i++) {

                    if (previousBeers.get(i).getQuantity() - beerlist.get(i).getQuantity() > 10) {
                        beerlist.get(i).setPrice(beerlist.get(i).getPrice() + 1);
                    } else if (previousBeers.get(i).getQuantity() - beerlist.get(i).getQuantity() < 10 && beerlist.get(i).getMinimalPrice() < beerlist.get(i).getPrice()) {
                        beerlist.get(i).setPrice(beerlist.get(i).getPrice() - 1);
                    }
                    Beer beer = modelMapper.fromBeerDtoToBeer(beerlist.get(i));
                    beerRepository.save(beer);

            }

        }

        try {
            statisticService.getLastReport();
        } catch (NullPointerException e) {
            statisticService.createNewReport();
        }

        ReportDto report = statisticService.getLastReport();
        statisticService.updateReportData(modelMapper.fromReportDtoToReport(report));
        previousBeers = this.beerService.getBeers();
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void changeMostPopularBeers() {
        try {
            statisticService.getLastReport();
        } catch (NullPointerException e) {
            statisticService.createNewReport();
        }

        ReportDto report = statisticService.getLastReport();

        if (this.previousBeersPopular != null) {
            List<Beer> beers = this.previousBeers.stream().map(beer -> modelMapper.fromBeerDtoToBeer(beer)).collect(Collectors.toList());
            List<Beer> actualBeers= beerRepository.findAll(); //after 1h
            List<Beer> mostPopularBeers = new LinkedList<>();

            for (int i=0; i<beers.size(); i++) {
                beers.get(i).setQuantity(beers.get(i).getQuantity() - actualBeers.get(i).getQuantity());
                mostPopularBeers = beers.stream().sorted(Comparator.comparingInt(Beer::getQuantity).reversed()).limit(3).collect(Collectors.toList());
            }

            report.setMostPopularBeers(mostPopularBeers.stream().map(beer -> beer.getBrand()).collect(Collectors.toList()));
            reportRepository.save(modelMapper.fromReportDtoToReport(report));

        }


        this.previousBeersPopular = this.beerService.getBeers();


    }



}
