package com.app.controller;

import com.app.model.dto.GroupDto;
import com.app.model.dto.UserDto;
import com.app.payloads.requests.AddUserGroupPayload;
import com.app.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/addUser")
    public GroupDto addUserToGroup(@RequestBody AddUserGroupPayload addUserGroupPayload) {
        return groupService.addUserToGroup(addUserGroupPayload.getEmail(), addUserGroupPayload.getGroupName());
    }

    @DeleteMapping("/deleteUser/{id}")
    public GroupDto deleteUserFromGroup(@PathVariable Long id, @RequestBody GroupDto groupDto) {
        return groupService.deleteUserFromGroup(id, groupDto);
    }

}
