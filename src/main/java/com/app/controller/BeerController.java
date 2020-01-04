package com.app.controller;

import com.app.model.Beer;
import com.app.model.dto.BeerDto;
import com.app.service.BeerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/beer")
@Api(tags = "Beer controller")
public class BeerController {
    private BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @ApiOperation(
            value = "Fetch all beers",
            response = Beer.class
    )
    @GetMapping
    public List<BeerDto> getAllBeers() {
        return beerService.getBeers();
    }

    @ApiOperation(
            value = "Add one beer",
            response = Beer.class
    )
    @PostMapping
    public BeerDto addBeer(@RequestPart(value = "file", required = false) MultipartFile uploadfile, @RequestParam("beerDto") String beerDto) throws IOException, IllegalAccessException {
        try {
            return beerService.addOrUpdateBeer(new ObjectMapper().readValue(beerDto, BeerDto.class), uploadfile);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(
            value = "Get one beer",
            response = Beer.class
    )
    @GetMapping("/{id}")
    public BeerDto getBeer(@PathVariable Long id) {
        return beerService.getBeer(id);
    }

    @ApiOperation(
            value = "Delete one beer",
            response = Beer.class
    )
    @DeleteMapping("/{id}")
    public BeerDto deleteBeer(@PathVariable Long id) {
        return beerService.deleteBeer(id);
    }

    @ApiOperation(
            value = "Update one beer",
            response = Beer.class
    )
    @PutMapping
    public BeerDto updateBeer(@RequestPart(value = "file", required = false) MultipartFile uploadfile, @RequestParam("beerDto") String beerDto) throws IOException, IllegalAccessException {
        try {
            return beerService.addOrUpdateBeer(new ObjectMapper().readValue(beerDto, BeerDto.class), uploadfile);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
