package com.app;

import com.app.model.*;
import com.app.model.dto.BeerDto;
import com.app.model.dto.OrderDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.LoginPayload;
import com.app.repository.BeerRepository;
import com.app.repository.OrderRepository;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRestControllerIntegrationTest {
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
    @Autowired
    private OrderRepository orderRepository;

    @Before
    public void init() {
        if (roleRepository.count() != RoleName.values().length) {
            roleRepository.deleteAll();
            Arrays
                    .stream(RoleName.values())
                    .forEach(role -> roleRepository.save(Role.builder().roleName(role).build()));
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.save(User.builder().email("test@test.com").username("test").roles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).build());
        Beer beer = beerRepository.save(Beer.builder().brand("Aaa").description("Adesc").quantity(10).price(10.0).orderItems(new LinkedList<>()).build());
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.INPROGRESS);
        OrderItem orderItem = OrderItem.builder().order(order).beer(beer).build();
        order.getOrderItems().add(orderItem);
        beer.setQuantity(beer.getQuantity() - 1);
        orderRepository.save(order);
        beerRepository.save(beer);
        userRepository.save(user);
    }

    @Test
    public void getOrdersTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        List<OrderDto> ordersDto = orderRepository.findAll().stream().map(modelMapper::fromOrderToOrderDto).collect(Collectors.toList());
        mvc.perform(get("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(ordersDto)));
        Assert.assertEquals(1, orderRepository.findAll().size());

    }

    @Test
    public void getOrderTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        OrderDto orderDto = orderRepository.findById(1L).map(modelMapper::fromOrderToOrderDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(orderDto)));
    }

    @Test
    public void createOrder() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        BeerDto beerDto = beerRepository.findById(1L).map(modelMapper::fromBeerToBeerDto).orElseThrow(NullPointerException::new);
        mvc.perform(post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(beerDto)))
                .andExpect(status().isOk());
        Assert.assertEquals(2, orderRepository.findAll().size());
        beerDto.setQuantity(beerDto.getQuantity()-1);
        Assert.assertEquals(beerDto,orderRepository.findById(2L).map(modelMapper::fromOrderToOrderDto).orElseThrow(NullPointerException::new).getOrderItemsDto().get(0).getBeerDto() );
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
