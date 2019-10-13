package com.app.service;

import com.app.exception.ResourceNotFoundException;
import com.app.model.Beer;
import com.app.model.dto.BeerDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerService {
    private BeerRepository beerRepository;
    private ModelMapper modelMapper;

    public BeerService(BeerRepository beerRepository, ModelMapper modelMapper) {
        this.beerRepository = beerRepository;
        this.modelMapper = modelMapper;
    }

    public List<BeerDto> getBeers() {
        return beerRepository
                .findAll()
                .stream()
                .map(modelMapper::fromBeerToBeerDto)
                .collect(Collectors.toList());
    }

    public BeerDto getBeer(Long id) {
        return beerRepository
                .findById(id)
                .map(modelMapper::fromBeerToBeerDto)
                .orElseThrow(NullPointerException::new);

    }

    public BeerDto addOrUpdateBeer(BeerDto beerDto) {
        if (beerDto == null)
            throw new NullPointerException("Beer is null");
        Beer beer = modelMapper.fromBeerDtoToBeer(beerDto);
        Beer beerFromDb = beerRepository.save(beer);
        return modelMapper.fromBeerToBeerDto(beerFromDb);
    }

    public BeerDto deleteBeer(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(NullPointerException::new);
        beerRepository.delete(beer);
        return modelMapper.fromBeerToBeerDto(beer);

    }
}
