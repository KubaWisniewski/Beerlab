package com.app.service;

import com.app.exception.CustomException;
import com.app.exception.ExceptionCode;
import com.app.exception.ExceptionInfo;
import com.app.model.Beer;
import com.app.repository.BeerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BeerService {
    private BeerRepository beerRepository;

    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public List<Beer> getBeers() {
        try {
            return beerRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(ExceptionInfo.builder()
                    .exceptionCode(ExceptionCode.SERVICE)
                    .exceptionDescription("GET BEERS, " + e.getMessage())
                    .exceptionDateTime(LocalDateTime.now())
                    .build());
        }
    }

    public Beer getBeer(Long id) {
        try {
            return beerRepository
                    .findById(id)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            throw new CustomException(ExceptionInfo.builder()
                    .exceptionCode(ExceptionCode.SERVICE)
                    .exceptionDescription("GET BEER, " + e.getMessage())
                    .exceptionDateTime(LocalDateTime.now())
                    .build());
        }
    }

    public Beer addBeer(Beer beer) {
        try {
            if (beer == null)
                throw new NullPointerException("Beer is null");
            return beerRepository.save(beer);
        } catch (Exception e) {
            throw new CustomException(ExceptionInfo.builder()
                    .exceptionCode(ExceptionCode.SERVICE)
                    .exceptionDescription("ADD BEER, " + e.getMessage())
                    .exceptionDateTime(LocalDateTime.now())
                    .build());
        }
    }

    public Beer updateBeer(Beer beer) {
        try {
            if (beer == null)
                throw new NullPointerException("Beer is null");
            return beerRepository.save(beer);
        } catch (Exception e) {
            throw new CustomException(ExceptionInfo.builder()
                    .exceptionCode(ExceptionCode.SERVICE)
                    .exceptionDescription("UPDATE BEER, " + e.getMessage())
                    .exceptionDateTime(LocalDateTime.now())
                    .build());
        }
    }

    public Beer deleteBeer(Long id) {
        try {
            Beer beer = beerRepository.findById(id).orElseThrow(NullPointerException::new);
            beerRepository.delete(beer);
            return beer;
        } catch (Exception e) {
            throw new CustomException(ExceptionInfo.builder()
                    .exceptionCode(ExceptionCode.SERVICE)
                    .exceptionDescription("DELETE BEER, " + e.getMessage())
                    .exceptionDateTime(LocalDateTime.now())
                    .build());
        }
    }
}
