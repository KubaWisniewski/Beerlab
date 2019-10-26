package com.app.controller;

import com.app.model.Group;
import com.app.model.dto.GroupDto;
import com.app.payloads.requests.AddOrDeleteUserGroupPayload;
import com.app.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/group")
@Api(tags = "Group controller")
public class GroupController {
    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation(
            value = "Get all groups",
            response = Group.class
    )
    @GetMapping
    public List<GroupDto> getAllGroups() {
        return groupService.getGroups();
    }

    @ApiOperation(
            value = "Add group",
            response = Group.class
    )
    @PostMapping
    public GroupDto addGroup(@RequestBody GroupDto groupDto) {
        return groupService.addOrUpdateGroup(groupDto);
    }

    @ApiOperation(
            value = "Get group by id",
            response = Group.class
    )
    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @ApiOperation(
            value = "Delete group by id",
            response = Group.class
    )
    @DeleteMapping("/{id}")
    public GroupDto deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id);
    }

    @ApiOperation(
            value = "Update group",
            response = Group.class
    )
    @PutMapping
    public GroupDto updateGroup(@ApiParam(value = "Group which need to be updated", required = true) @RequestBody GroupDto groupDto) {
        return groupService.addOrUpdateGroup(groupDto);
    }

    @ApiOperation(
            value = "Add user to the group ",
            response = Group.class
    )
    @PutMapping("/addUser")
    public GroupDto addUserToGroup(@RequestBody AddOrDeleteUserGroupPayload addOrDeleteUserGroupPayload) {
        return groupService.addUserToGroup(addOrDeleteUserGroupPayload.getEmail(), addOrDeleteUserGroupPayload.getGroupName());
    }

    @ApiOperation(
            value = "Delete user from the group ",
            response = Group.class
    )
    @DeleteMapping("/deleteUser")
    public GroupDto deleteUserFromGroup(@RequestBody AddOrDeleteUserGroupPayload addOrDeleteUserGroupPayload) {
        return groupService.deleteUserFromGroup(addOrDeleteUserGroupPayload.getEmail(), addOrDeleteUserGroupPayload.getGroupName());
    }
}
