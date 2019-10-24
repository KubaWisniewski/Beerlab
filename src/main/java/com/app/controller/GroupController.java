package com.app.controller;

import com.app.model.dto.GroupDto;
import com.app.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDto> getAllGroups() {
        return groupService.getGroups();
    }

    @PostMapping
    public GroupDto addGroup(@RequestBody GroupDto groupDto) {
        return groupService.addOrUpdateGroup(groupDto);
    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @DeleteMapping("/{id}")
    public GroupDto deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id);
    }

    @PutMapping
    public GroupDto updateGroup(@RequestBody GroupDto groupDto) {
        return groupService.addOrUpdateGroup(groupDto);
    }

}
