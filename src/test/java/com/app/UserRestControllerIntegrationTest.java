package com.app;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.model.dto.UserDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.LoginPayload;
import com.app.payloads.requests.RegisterPayload;
import com.app.payloads.responses.ApiPayload;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BeerRepository beerRepository;
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
        userRepository.save(User.builder().email("test@test.com").username("test").roles(Sets.newSet(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).balance(100.0).build());
        beerRepository.save(Beer.builder().brand("Aaa").description("Adesc").quantity(10).price(10.0).build());
    }

    @Test
    public void signInTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        UserDto userDto = userRepository.findById(1L).map(modelMapper::fromUserToUserDto).orElseThrow(NullPointerException::new);
        mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(LoginPayload.builder().email("test@test.com").password("123").build())))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(userDto)));
    }

    @Test
    public void signUpTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        mvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(RegisterPayload.builder().email("newTest@test.com").username("newTest").password("123").build())))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(ApiPayload.builder().success(true).message("User registered successfully").build())));
        Assert.assertEquals(2, userRepository.findAll().size());
    }

    @Test
    public void getUserInformationTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        UserDto userDto = userRepository.findById(1L).map(modelMapper::fromUserToUserDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/user/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(userDto)));
    }

    @Test
    public void getUserBalanceTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        UserDto userDto = userRepository.findById(1L).map(modelMapper::fromUserToUserDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/user/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(userDto.getBalance())));
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
