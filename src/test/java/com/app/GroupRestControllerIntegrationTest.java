package com.app;

import com.app.model.*;
import com.app.model.dto.GroupDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.AddOrDeleteUserGroupPayload;
import com.app.payloads.requests.LoginPayload;
import com.app.repository.BeerRepository;
import com.app.repository.GroupRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GroupRestControllerIntegrationTest {
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
    @Autowired
    private GroupRepository groupRepository;

    @Before
    public void init() {
        if (roleRepository.count() != RoleName.values().length) {
            roleRepository.deleteAll();
            Arrays
                    .stream(RoleName.values())
                    .forEach(role -> roleRepository.save(Role.builder().roleName(role).build()));
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = User.builder().email("test@test.com").username("test").roles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).balance(100.0).build();
        beerRepository.save(Beer.builder().brand("Aaa").description("Adesc").quantity(10).price(10.0).build());
        Group group = Group.builder().name("TestGroup").description("Test description").members(Collections.singletonList(user)).build();
        user.setGroup(group);
        groupRepository.save(group);
        userRepository.save(user);
    }

    @Test
    public void getGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        GroupDto groupDto = groupRepository.findById(1L).map(modelMapper::fromGroupToGroupDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(groupDto)));
    }

    @Test
    public void getGroupsTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        GroupDto groupDto = groupRepository.findById(1L).map(modelMapper::fromGroupToGroupDto).orElseThrow(NullPointerException::new);
        mvc.perform(get("/api/group")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(Collections.singletonList(groupDto))));
    }

    @Test
    public void deleteGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        final int countBefore = beerRepository.findAll().size();
        GroupDto groupDto = groupRepository.findById(1L).map(modelMapper::fromGroupToGroupDto).orElseThrow(NullPointerException::new);
        mvc.perform(delete("/api/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(groupDto)));
        Assert.assertEquals(countBefore - 1, groupRepository.findAll().size());
    }

    @Test
    public void addGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        GroupDto groupDto = GroupDto.builder().description("Test description").name("Test").build();
        mvc.perform(post("/api/group")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(groupDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(groupDto)));
        Assert.assertEquals(2, groupRepository.findAll().size());
    }

    @Test
    public void updateGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        final String updateDesc = "UpdateDesc";
        GroupDto groupDto = groupRepository.findById(1L).map(modelMapper::fromGroupToGroupDto).orElseThrow(NullPointerException::new);
        groupDto.setDescription(updateDesc);
        mvc.perform(put("/api/group")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(groupDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(gsonBuilder.toJson(groupDto)));
        Assert.assertEquals(1, groupRepository.findAll().size());
        Assert.assertEquals(updateDesc, groupRepository.findById(1L).get().getDescription());
    }

    @Test
    public void addUserToGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder().email("add@test.com").username("AddTest").roles(Collections.singletonList(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).balance(100.0).build());
        mvc.perform(put("/api/group/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(AddOrDeleteUserGroupPayload.builder().email("add@test.com").groupName("TestGroup").build())))
                .andExpect(status().isOk());
        Assert.assertEquals(2, groupRepository.findById(1L).get().getMembers().size());
    }

    @Test
    public void deleteUserToGroupTest() throws Exception {
        Gson gsonBuilder = new GsonBuilder().create();
        mvc.perform(delete("/api/group/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Auth-Token", getAuthToken())
                .header("Accept", "application/json")
                .content(gsonBuilder.toJson(AddOrDeleteUserGroupPayload.builder().email("test@test.com").groupName("TestGroup").build())))
                .andExpect(status().isOk());
        Assert.assertEquals(0, groupRepository.findById(1L).get().getMembers().size());
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

