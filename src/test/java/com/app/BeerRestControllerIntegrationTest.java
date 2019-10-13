package com.app;

import com.app.model.Beer;
import com.app.repository.BeerRepository;
import com.app.service.BeerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BeerRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BeerService beerService;
    @MockBean
    private BeerRepository beerRepository;

    @Test
    public void getBeersTest() throws Exception {
        Beer beer = Beer.builder().brand("Tyskie").description("Dobre piwo").id(1L).imgUrl("img1.png").price(15.5).quantity(5).build();
        Beer beer2 = Beer.builder().brand("Lech").description("Zwykle piwo").id(2L).imgUrl("img2.png").price(10.0).quantity(10).build();

        List<Beer> beers = Arrays.asList(beer, beer2);
        given(beerService.getBeers()).willReturn(beers);

        mvc.perform(get("/api/beer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id': 1,'brand': 'Tyskie','imgUrl': 'img1.png', 'quantity': 5, 'price': 15.5}, {'id': 2,'brand': 'Lech','imgUrl': 'img2.png', 'quantity': 10, 'price': 10.0}]"));
    }

    //https://www.springboottutorial.com/unit-testing-for-spring-boot-rest-services
    @Test
    public void addBeerTest() throws Exception {
        /*
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/beer)")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(exampleBeerJSON);
                */


/*
        mvc.perform(post("/api/beer"))
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id': 1,'brand': 'Tyskie','imgUrl': 'img1.png', 'quantity': 5, 'price': 15.5}]"));
                */

    }

    //https://stackoverflow.com/questions/45241566/spring-boot-unit-tests-with-jwt-token-security
    @Test
    public void delete_beerTest() throws Exception {
        doNothing().when(beerRepository).deleteById(1L);

        mvc.perform(delete("/beer/1"))
                .andExpect(status().isOk());

        verify(beerRepository, times(1)).deleteById(1L);

    }
}
