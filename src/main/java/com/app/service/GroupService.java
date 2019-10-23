package com.app.service;

import com.app.model.Beer;
import com.app.model.Group;
import com.app.model.dto.GroupDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private ModelMapper modelMapper;

    public GroupService(GroupRepository groupRepository, ModelMapper modelMapper) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
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

    public GroupDto deleteBeer(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(NullPointerException::new);
        groupRepository.delete(group);
        return modelMapper.fromGroupToGroupDto(group);
    }
}
