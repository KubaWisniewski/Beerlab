package com.app.controller;

import com.app.model.Group;
import com.app.model.Message;
import com.app.model.dto.MessageDto;
import com.app.payloads.requests.MessagePayload;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import com.app.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@Api(tags = "Message controller")
public class MessageController {
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation(
            value = "Send message",
            response = Message.class
    )
    @PostMapping
    public MessageDto addMessage(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @RequestBody MessagePayload messagePayload) {
        return messageService.addMessage(userDetails.getId(), messagePayload);
    }
    @ApiOperation(
            value = "Get messages",
            response = Group.class
    )
    @GetMapping(("/{groupId}"))
    public List<MessageDto> getMessagesByGroup(@PathVariable Long groupId) {
        return messageService.getMessagesByGroup(groupId);
    }
}
