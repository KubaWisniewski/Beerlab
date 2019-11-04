package com.app.service;

import com.app.model.Group;
import com.app.model.User;
import com.app.model.dto.GroupDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.GroupRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public List<GroupDto> getGroups() {
        return groupRepository
                .findAll()
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

    public GroupDto addOrUpdateGroup(GroupDto groupDto) {
        if (groupDto == null)
            throw new NullPointerException("Beer is null");
        Group group = modelMapper.fromGroupDtoToGroup(groupDto);
        groupRepository.save(group);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto deleteGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(NullPointerException::new);
        groupRepository.delete(group);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto addUserToGroup(String email, String groupName) {
        if (email == null && groupName == null) {
            throw new NullPointerException("User email and group null");
        }
        Group group = groupRepository.findByName(groupName);
        User userToAdd = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not exist"));
        group.getMembers().add(userToAdd);
        userToAdd.setGroup(group);
        userRepository.save(userToAdd);
        groupRepository.save(group);
        return modelMapper.fromGroupToGroupDto(group);
    }

    public GroupDto deleteUserFromGroup(String email, String groupName) {
        if (email == null && groupName == null) {
            throw new NullPointerException("User email and group null");
        }
        Group group = groupRepository.findByName(groupName);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not exist"));
        user.setGroup(null);
        group.getMembers().remove(user);
        groupRepository.save(group);
        userRepository.save(user);
        return modelMapper.fromGroupToGroupDto(group);
    }
}
