package com.app;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.payloads.requests.CreateWorkerAccountPayload;
import com.app.payloads.requests.LoginPayload;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminRestControllerIntegrtionTest {
    @Autowired
    private MockMvc mvc;
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
        userRepository.save(User.builder().email("test@test.com").username("test").roles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).get())).password(bCryptPasswordEncoder.encode("123")).balance(100.0).build());
        beerRepository.save(Beer.builder().brand("Aaa").description("Adesc").quantity(10).price(10.0).build());
    }


    @Test
    public void createWorkerTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        mvc.perform(post("/api/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(CreateWorkerAccountPayload.builder().email("newWorker@test.com").username("newWorker").role("ROLE_BARMAN").password("123").build())))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(ApiPayload.builder().success(true).message("User registered successfully").build())));
        Assert.assertEquals(2, userRepository.findAll().size());
        Assert.assertEquals("newWorker@test.com", userRepository.findById(2L).get().getEmail());
        Assert.assertEquals("ROLE_BARMAN", userRepository.findById(2L).get().getRoles().get(0).getRoleName().toString());
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
