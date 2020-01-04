package com.app.model.modelMappers;

import com.app.model.*;
import com.app.model.dto.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ModelMapper {
    public UserDto fromUserToUserDto(User user) {
        return user ==
                null ? null : UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .balance(user.getBalance())
                .password(user.getPassword())
                .username(user.getUsername())
                .rolesDto(user.getRoles() == null ? null : user.getRoles().stream().map(this::fromRoleToRoleDto).collect(Collectors.toList()))
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }

    public User fromUserDtoToUser(UserDto userDto) {
        return userDto ==
                null ? null : User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .balance(userDto.getBalance())
                .username(userDto.getUsername())
                .roles(userDto.getRolesDto() == null ? null : userDto.getRolesDto().stream().map(this::fromRoleDtoToRole).collect(Collectors.toList()))
                .gender(userDto.getGender())
                .dateOfBirth(userDto.getDateOfBirth())
                .build();
    }


    public RoleDto fromRoleToRoleDto(Role role) {
        return role ==
                null ? null : RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public Role fromRoleDtoToRole(RoleDto roleDto) {
        return roleDto ==
                null ? null : Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .build();
    }

    public MessageDto fromMessageToMessageDto(Message message) {
        return message ==
                null ? null : MessageDto.builder()
                .id(message.getId())
                .text(message.getText())
                .time(message.getTime())
                .userEmail(message.getUser() == null ? "" : message.getUser().getEmail())
                .build();
    }

    public Message fromMessageDtoToMessage(MessageDto messageDto) {
        return messageDto ==
                null ? null : Message.builder()
                .id(messageDto.getId())
                .text(messageDto.getText())
                .time(messageDto.getTime())
                .build();
    }

    public OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return orderItem ==
                null ? null : OrderItemDto.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .beerDto(orderItem.getBeer() == null ? null : fromBeerToBeerDto(orderItem.getBeer()))
                .build();
    }

    public OrderItem fromOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        return orderItemDto ==
                null ? null : OrderItem.builder()
                .id(orderItemDto.getId())
                .quantity(orderItemDto.getQuantity())
                .unitPrice(orderItemDto.getUnitPrice())
                .beer(orderItemDto.getBeerDto() == null ? null : fromBeerDtoToBeer(orderItemDto.getBeerDto()))
                .build();
    }

    public OrderDto fromOrderToOrderDto(Order order) {
        return order ==
                null ? null : OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus())
                .orderItemsDto(order.getOrderItems() == null ? null : order.getOrderItems().stream().map(this::fromOrderItemToOrderItemDto).collect(Collectors.toList()))
                .completeTime(order.getCompleteTime())
                .startedTime(order.getStartedTime())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public Order fromOrderDtoToOrder(OrderDto orderDto) {
        return orderDto
                ==
                null ? null : Order.builder()
                .id(orderDto.getId())
                .status(orderDto.getStatus())
                .orderItems(orderDto.getOrderItemsDto() == null ? null : orderDto.getOrderItemsDto().stream().map(this::fromOrderItemDtoToOrderItem).collect(Collectors.toList()))
                .completeTime(orderDto.getCompleteTime())
                .startedTime(orderDto.getStartedTime())
                .totalPrice(orderDto.getTotalPrice())
                .build();
    }

    public BeerDto fromBeerToBeerDto(Beer beer) {
        return beer ==
                null ? null : BeerDto.builder()
                .id(beer.getId())
                .brand(beer.getBrand())
                .description(beer.getDescription())
                .imgUrl(beer.getImgUrl())
                .price(beer.getPrice())
                .quantity(beer.getQuantity())
                .build();
    }

    public Beer fromBeerDtoToBeer(BeerDto beerDto) {
        return beerDto ==
                null ? null : Beer.builder()
                .id(beerDto.getId())
                .brand(beerDto.getBrand())
                .description(beerDto.getDescription())
                .imgUrl(beerDto.getImgUrl())
                .price(beerDto.getPrice())
                .quantity(beerDto.getQuantity())
                .build();
    }

    public GroupDto fromGroupToGroupDto(Group group) {
        return group == null ? null : GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .build();
    }

    public Group fromGroupDtoToGroup(GroupDto groupDto) {
        return groupDto == null ? null : Group.builder()
                .id(groupDto.getId())
                .name(groupDto.getName())
                .description(groupDto.getDescription())
                .build();
    }

    public Report fromReportDtoToReport(ReportDto reportDto) {
        return reportDto == null ? null : Report.builder()
                .id(reportDto.getId())
                .avgBeerPrice(reportDto.getAvgBeerPrice())
                .start(reportDto.getStart())
                .end(reportDto.getEnd())
                .orders(reportDto.getOrders())
                .users(reportDto.getUsers())
                .build();
    }

    public ReportDto fromReportToReportDto(Report report) {
        return report == null ? null : ReportDto.builder()
                .id(report.getId())
                .avgBeerPrice(report.getAvgBeerPrice())
                .start(report.getStart())
                .end(report.getEnd())
                .orders(report.getOrders())
                .users(report.getUsers())
                .build();
    }

    public QuizDto fromQuizToQuizDto(Quiz quiz) {
        return quiz == null ? null : QuizDto.builder()
                .id(quiz.getId())
                .name(quiz.getName())
                .description(quiz.getDescription())
                .startDate(quiz.getStartDate())
                .endDate(quiz.getEndDate())
                .imgUrl(quiz.getImgUrl())
                .isActive(quiz.isActive())
                .questionDtoList(quiz.getQuestions() == null ? null : quiz.getQuestions().stream().map(this::fromQuestionToQuestionDto).collect(Collectors.toList()))
                .build();
    }

    public Quiz fromQuizDtoToQuiz(QuizDto quizDto) {
        return quizDto == null ? null : Quiz.builder()
                .id(quizDto.getId())
                .name(quizDto.getName())
                .description(quizDto.getDescription())
                .startDate(quizDto.getStartDate())
                .endDate(quizDto.getEndDate())
                .imgUrl(quizDto.getImgUrl())
                .isActive(quizDto.isActive())
                .questions(quizDto.getQuestionDtoList() == null ? null : quizDto.getQuestionDtoList().stream().map(this::fromQuestionDtoToQuestion).collect(Collectors.toList()))
                .build();
    }

    public QuestionDto fromQuestionToQuestionDto(Question question) {
        return question == null ? null : QuestionDto.builder()
                .id(question.getId())
                .text(question.getText())
                .imgUrl(question.getImgUrl())
                .answerDtoList(question.getAnswers() == null ? null : question.getAnswers().stream().map(this::fromAnswerToAnswerDto).collect(Collectors.toList()))
                .build();
    }

    public Question fromQuestionDtoToQuestion(QuestionDto questionDto) {
        return questionDto == null ? null : Question.builder()
                .id(questionDto.getId())
                .text(questionDto.getText())
                .imgUrl(questionDto.getImgUrl())
                .answers(questionDto.getAnswerDtoList() == null ? null : questionDto.getAnswerDtoList().stream().map(this::fromAnswerDtoToAnswer).collect(Collectors.toList()))
                .build();
    }

    public Answer fromAnswerDtoToAnswer(AnswerDto answerDto) {
        return answerDto == null ? null : Answer.builder()
                .id(answerDto.getId())
                .text(answerDto.getText())
                .isCorrect(answerDto.isCorrect())
                .imgUrl(answerDto.getImgUrl())
                .build();
    }

    public AnswerDto fromAnswerToAnswerDto(Answer answer) {
        return answer == null ? null : AnswerDto.builder()
                .id(answer.getId())
                .text(answer.getText())
                .isCorrect(answer.isCorrect())
                .imgUrl(answer.getImgUrl())
                .build();
    }

    public UserQuizDto fromUserQuizToUserQuizDto(UserQuiz userQuiz) {
        return userQuiz == null ? null : UserQuizDto.builder()
                .id(userQuiz.getId())
                .score(userQuiz.getScore())
                .quizDto(userQuiz.getQuiz() == null ? null : fromQuizToQuizDto(userQuiz.getQuiz()))
                .build();
    }


    public UserQuiz fromUserQuizDtoToUserQuiz(UserQuizDto userQuizDto) {
        return userQuizDto == null ? null : UserQuiz.builder()
                .id(userQuizDto.getId())
                .score(userQuizDto.getScore())
                .user(userQuizDto.getUserDto() == null ? null : fromUserDtoToUser(userQuizDto.getUserDto()))
                .quiz(userQuizDto.getQuizDto() == null ? null : fromQuizDtoToQuiz(userQuizDto.getQuizDto()))
                .userAnswers(userQuizDto.getUserAnswerDtos() == null ? null : userQuizDto.getUserAnswerDtos().stream().map(this::formUserAnswerDtoToUserAnswer).collect(Collectors.toList()))
                .build();
    }

    public UserAnswerDto formUserAnswerToUserAnswerDto(UserAnswer userAnswer) {
        return userAnswer == null ? null : UserAnswerDto.builder()
                .id(userAnswer.getId())
                .questionDto(userAnswer.getQuestion() == null ? null : fromQuestionToQuestionDto(userAnswer.getQuestion()))
                .answerDto(userAnswer.getAnswer() == null ? null : fromAnswerToAnswerDto(userAnswer.getAnswer()))
                .userQuizDto(userAnswer.getUserQuiz() == null ? null : fromUserQuizToUserQuizDto(userAnswer.getUserQuiz()))
                .build();
    }

    public UserAnswer formUserAnswerDtoToUserAnswer(UserAnswerDto userAnswerDto) {
        return userAnswerDto == null ? null : UserAnswer.builder()
                .id(userAnswerDto.getId())
                .question(userAnswerDto.getQuestionDto() == null ? null : fromQuestionDtoToQuestion(userAnswerDto.getQuestionDto()))
                .answer(userAnswerDto.getAnswerDto() == null ? null : fromAnswerDtoToAnswer(userAnswerDto.getAnswerDto()))
                .userQuiz(userAnswerDto.getUserQuizDto() == null ? null : fromUserQuizDtoToUserQuiz(userAnswerDto.getUserQuizDto()))
                .build();
    }
}
