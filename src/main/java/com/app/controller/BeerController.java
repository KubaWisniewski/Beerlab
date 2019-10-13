package com.app.controller;

import com.app.model.dto.BeerDto;
import com.app.service.BeerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beer")
public class BeerController {
    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping
    public List<BeerDto> getAllBeers() {
        return beerService.getBeers();
    }

    @PostMapping
    public BeerDto addBeer(@RequestBody BeerDto beerDto) {
        return beerService.addOrUpdateBeer(beerDto);
    }

    @GetMapping("/{id}")
    public BeerDto getBeer(@PathVariable Long id) {
        return beerService.getBeer(id);
    }

    @DeleteMapping("/{id}")
    public BeerDto deleteBeer(@PathVariable Long id) {
        return beerService.deleteBeer(id);
    }

    @PutMapping
    public BeerDto updateBeer(@RequestBody BeerDto beerDto) {
        return beerService.addOrUpdateBeer(beerDto);
    }
}
