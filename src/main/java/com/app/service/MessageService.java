package com.app.service;

import com.app.model.Message;
import com.app.model.dto.MessageDto;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.MessagePayload;
import com.app.repository.GroupRepository;
import com.app.repository.MessageRepository;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private ModelMapper modelMapper;


    public MessageService(MessageRepository messageRepository, UserRepository userRepository, GroupRepository groupRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
    }


    public MessageDto addMessage(Long userId, MessagePayload messagePayload) {
        Message message = messageRepository.save(Message.builder()
                .text(messagePayload.getText())
                .time(messagePayload.getTime())
                .user(userRepository.findById(userId).orElseThrow(NullPointerException::new))
                .group(groupRepository.findById(messagePayload.getGroupId()).orElseThrow(NullPointerException::new)).build());
        return modelMapper.fromMessageToMessageDto(message);
    }

    public List<MessageDto> getMessagesByGroup(Long groupId) {
        return messageRepository
                .findAllByGroup_Id(groupId)
                .stream()
                .map(modelMapper::fromMessageToMessageDto)
                .collect(Collectors.toList());
    }
}
