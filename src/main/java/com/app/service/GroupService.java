package com.app.service;

import com.app.model.Group;
import com.app.model.User;
import com.app.model.dto.GroupDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.CreateGroupPayload;
import com.app.repository.GroupRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public List<GroupDto> getGroupsByUser(Long userId) {
        return groupRepository
                .findGroupsByUsersContains(userRepository.findById(userId).get())
                .stream()
                .map(modelMapper::fromGroupToGroupDto)
                .collect(Collectors.toList());
    }

    public GroupDto getGroup(Long id) {
        return groupRepository
                .findById(id)
                .map(modelMapper::fromGroupToGroupDto)
                .orElseThrow(NullPointerException::new);
    }

    public GroupDto createGroup(Long userId, CreateGroupPayload createGroupPayload) {
        if (createGroupPayload == null)
            throw new NullPointerException("Group is null");
        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        Group group = Group.builder().name(createGroupPayload.getName()).description(createGroupPayload.getDescription()).users(new ArrayList<>(Collections.singletonList(user))).build();
        user.getGroups().add(group);
        groupRepository.save(group);
        userRepository.save(user);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto deleteGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(NullPointerException::new);
        groupRepository.delete(group);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto addUserToGroup(String email, Long groupId) {
        if (email == null && groupId == null) {
            throw new NullPointerException("User email and group null");
        }
        Group group = groupRepository.findById(groupId).orElseThrow(NullPointerException::new);
        User userToAdd = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not exist"));
        group.getUsers().add(userToAdd);
        userToAdd.getGroups().add(group);
        userRepository.save(userToAdd);
        groupRepository.save(group);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto deleteUserFromGroup(String email, Long groupId) {
        if (email == null && groupId == null) {
            throw new NullPointerException("User email and group null");
        }
        Group group = groupRepository.findById(groupId).orElseThrow(NullPointerException::new);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not exist"));
        user.getGroups().remove(group);
        group.getUsers().remove(user);
        groupRepository.save(group);
        userRepository.save(user);
        return modelMapper.fromGroupToGroupDto(group);
    }
}
