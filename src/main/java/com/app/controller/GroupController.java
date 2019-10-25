package com.app.controller;

import com.app.model.Beer;
import com.app.model.Group;
import com.app.model.dto.GroupDto;
import com.app.model.dto.UserDto;
import com.app.payloads.requests.AddUserGroupPayload;
import com.app.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
@Api(tags = "Group controller")
public class GroupController {
    GroupService groupService;

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
    public GroupDto addUserToGroup(@RequestBody AddUserGroupPayload addUserGroupPayload) {
        return groupService.addUserToGroup(addUserGroupPayload.getEmail(), addUserGroupPayload.getGroupName());
    }

    @ApiOperation(
            value = "Delete user from the group ",
            response = Group.class
    )
    @DeleteMapping("/deleteUser/{id}")
    public GroupDto deleteUserFromGroup(@ApiParam(value = "User id", required = true) @PathVariable Long id, @ApiParam(value = "Group object", required = true) @RequestBody GroupDto groupDto) {
        return groupService.deleteUserFromGroup(id, groupDto);
    }

}
