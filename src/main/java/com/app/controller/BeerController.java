package com.app.controller;

import com.app.model.Beer;
import com.app.service.BeerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beer")
@CrossOrigin
public class BeerController {
    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping
    public List<Beer> getAllBeers() {
        return beerService.getBeers();
    }

    @PostMapping
    public Beer addBeer(@RequestBody Beer beer) {
        return beerService.addBeer(beer);
    }

    @GetMapping("/{id}")
    public Beer getBeer(@PathVariable Long id) {
        return beerService.getBeer(id);
    }

    @DeleteMapping("/{id}")
    public Beer deleteBeer(@PathVariable Long id) {
        return beerService.deleteBeer(id);
    }

    @PutMapping
    public Beer updateBeer(@RequestBody Beer beer) {
        return beerService.updateBeer(beer);
    }
}
