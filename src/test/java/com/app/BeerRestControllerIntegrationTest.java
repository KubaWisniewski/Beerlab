package com.app;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.model.dto.BeerDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.LoginPayload;
import com.app.repository.BeerRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BeerRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private BeerRepository beerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        if (roleRepository.count() != RoleName.values().length) {
            roleRepository.deleteAll();
            Arrays
                    .stream(RoleName.values())
                    .forEach(role -> roleRepository.save(Role.builder().roleName(role).build()));
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder().email("test@test.com").username("test").roles(Sets.newSet(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).build());
        beerRepository.save(Beer.builder().brand("Aaa").description("Adesc").quantity(10).price(10.0).build());
    }

    @Test
    public void getBeersTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        List<BeerDto> beers = beerRepository.findAll().stream().map(modelMapper::fromBeerToBeerDto).collect(Collectors.toList());
        mvc.perform(get("/api/beer")
                .contentType(MediaType.APPLICATION_JSON).header("X-Auth-Token", getAuthToken()).header("Accept", "application/json"))
                .andExpect(content().json(gsonBuilder.toJson(beers)));
    }

    @Test
    public void getBeerTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        BeerDto beer = beerRepository.findById(1L).map(modelMapper::fromBeerToBeerDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/beer/1")
                .contentType(MediaType.APPLICATION_JSON).header("X-Auth-Token", getAuthToken()).header("Accept", "application/json"))
                .andExpect(content().json(gsonBuilder.toJson(beer)));
    }

    @Test
    public void addBeerTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        BeerDto beerDto = BeerDto.builder().brand("Test").description("TestDesc").price(10.0).quantity(10).imgUrl("TestImg.png").build();
        mvc.perform(post("/api/beer").contentType(MediaType.APPLICATION_JSON).header("X-Auth-Token", getAuthToken()).header("Accept", "application/json").content(gsonBuilder.toJson(beerDto).toString()))
                .andExpect(content().json(gsonBuilder.toJson(beerDto)));
    }

    @Test
    public void deleteBeerTest() throws Exception {
        final int countBefore = beerRepository.findAll().size();
        mvc.perform(delete("/api/beer/1")
                .contentType(MediaType.APPLICATION_JSON).header("X-Auth-Token", getAuthToken()).header("Accept", "application/json"))
                .andExpect(status().isOk());
        Assert.assertEquals(countBefore - 1, beerRepository.findAll().size());
    }

    private String getAuthToken() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        return mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gsonBuilder.toJson(LoginPayload.builder()
                        .email("test@test.com")
                        .password("123").build()))).andReturn()
                .getResponse()
                .getHeader("x-auth-token");
    }
}
