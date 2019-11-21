package com.app.service;

import com.app.model.Beer;
import com.app.model.dto.BeerDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.BeerRepository;
import com.app.utils.FileManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeerService {
    private BeerRepository beerRepository;
    private ModelMapper modelMapper;
    private FileManager fileManager;
    private AmazonClient amazonClient;

    public BeerService(BeerRepository beerRepository, ModelMapper modelMapper, FileManager fileManager, AmazonClient amazonClient) {
        this.beerRepository = beerRepository;
        this.modelMapper = modelMapper;
        this.fileManager = fileManager;
        this.amazonClient = amazonClient;
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

    public BeerDto addOrUpdateBeer(BeerDto beerDto, MultipartFile multipartFile) throws IOException, IllegalAccessException {
        if (beerDto == null)
            throw new NullPointerException("Beer is null");
        Beer beer = modelMapper.fromBeerDtoToBeer(beerDto);
        if (multipartFile != null && beerDto.getImgUrl() == null)
            beer.setImgUrl(amazonClient.uploadFile(multipartFile));
        else if (multipartFile != null && !beerDto.getImgUrl().equals("")) {
            amazonClient.deleteFileFromS3Bucket(beerDto.getImgUrl());
            beer.setImgUrl(amazonClient.uploadFile(multipartFile));
        }
        Beer beerFromDb = beerRepository.save(beer);
        return modelMapper.fromBeerToBeerDto(beerFromDb);
    }

    public BeerDto deleteBeer(Long id) {
        Beer beer = beerRepository.findById(id).orElseThrow(NullPointerException::new);
        amazonClient.deleteFileFromS3Bucket(beer.getImgUrl());
        beerRepository.delete(beer);
        return modelMapper.fromBeerToBeerDto(beer);
    }
}
