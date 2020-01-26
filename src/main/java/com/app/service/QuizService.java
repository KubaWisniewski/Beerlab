package com.app.service;

import com.app.model.*;
import com.app.model.dto.*;
import com.app.model.modelMappers.ModelMapper;
import com.app.payloads.requests.CreateAnswerPayload;
import com.app.payloads.requests.CreateQuestionPayload;
import com.app.payloads.requests.CreateQuizPayload;
import com.app.payloads.responses.UserScore;
import com.app.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private ModelMapper modelMapper;
    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserQuizRepository userQuizRepository;
    private UserAnswerRepository userAnswerRepository;

    public QuizService(ModelMapper modelMapper, QuizRepository quizRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository, RoleRepository roleRepository, UserQuizRepository userQuizRepository, UserAnswerRepository userAnswerRepository) {
        this.modelMapper = modelMapper;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userQuizRepository = userQuizRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream().map(modelMapper::fromQuizToQuizDto).collect(Collectors.toList());
    }

    public List<QuizDto> getQuizesForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User does not exist"));
        return quizRepository
                .findAll()
                .stream()
                .filter(x -> !x.getUserQuizzes().stream().map(UserQuiz::getUser).collect(Collectors.toList()).contains(user) && x.isActive())
                .map(modelMapper::fromQuizToQuizDto).collect(Collectors.toList());
    }

    public QuizDto getQuizById(Long id) {
        return modelMapper.fromQuizToQuizDto(quizRepository.findById(id).orElseThrow(() -> new NullPointerException("quiz does not exist")));
    }

    public List<UserScore> getUsersScore() {
        List<User> users = userRepository.findAllByRolesIsContaining(roleRepository.findByRoleName(RoleName.ROLE_USER).get());
        return users.stream().map(x -> UserScore.builder().username(x.getUsername()).totalScore(x.getUserQuizzes().stream().mapToInt(UserQuiz::getScore).sum()).numberOfDoneQuizes(x.getUserQuizzes().size()).build()).collect(Collectors.toList());
    }

    public QuizDto updateQuiz(Long id, CreateQuizPayload createQuizPayload) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NullPointerException("Quiz does not find"));
        quiz.setImgUrl(createQuizPayload.getImgUrl());
        quiz.setName(createQuizPayload.getName());
        quiz.setDescription(createQuizPayload.getDescription());
        quiz.setActive(createQuizPayload.isActive());
        quiz.setStartDate(createQuizPayload.getStartDate());
        quiz.setEndDate(createQuizPayload.getEndDate());
        quizRepository.save(quiz);
        return modelMapper.fromQuizToQuizDto(quiz);

    }

    public List<QuestionDto> getAllQuestionsByQuizId(Long quizId) {
        return questionRepository.findAllByQuiz_Id(quizId).stream().map(modelMapper::fromQuestionToQuestionDto).collect(Collectors.toList());
    }

    public List<AnswerDto> getAllAnswersByQuestionId(Long questionId) {
        return answerRepository.findAllByQuestion_Id(questionId).stream().map(modelMapper::fromAnswerToAnswerDto).collect(Collectors.toList());
    }

    public QuizDto changeStatus(Long id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new NullPointerException("quiz does not exist"));
        quiz.setActive(!quiz.isActive());
        return modelMapper.fromQuizToQuizDto(quizRepository.save(quiz));
    }

    public QuizDto createQuiz(CreateQuizPayload createQuizPayload) {
        Quiz quiz = Quiz.builder()
                .name(createQuizPayload.getName())
                .description(createQuizPayload.getDescription())
                .startDate(createQuizPayload.getStartDate())
                .endDate(createQuizPayload.getEndDate())
                .imgUrl(createQuizPayload.getImgUrl())
                .isActive(createQuizPayload.isActive())
                .build();

        return modelMapper.fromQuizToQuizDto(quizRepository.save(quiz));
    }

    public AnswerDto addAnswer(Long questionId, CreateAnswerPayload createAnswerPayload) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new NullPointerException("Question does not exist"));
        Answer answer = Answer.builder()
                .question(question)
                .text(createAnswerPayload.getText())
                .imgUrl(createAnswerPayload.getImgUrl())
                .isCorrect(createAnswerPayload.isCorrect())
                .build();
        return modelMapper.fromAnswerToAnswerDto(answerRepository.saveAndFlush(answer));
    }

    public QuestionDto addQuestion(Long quizId, CreateQuestionPayload createQuestionPayload) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new NullPointerException("Quiz does not exist"));
        Question question = Question.builder()
                .quiz(quiz)
                .text(createQuestionPayload.getText())
                .imgUrl(createQuestionPayload.getImgUrl())
                .build();
        return modelMapper.fromQuestionToQuestionDto(questionRepository.saveAndFlush(question));
    }

    public AnswerDto updateAnswer(Long questionId, Long answerId, CreateAnswerPayload createAnswerPayload) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new NullPointerException("Question does not exist"));
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new NullPointerException("Answer does not exist."));
        answer.setQuestion(question);
        answer.setCorrect(createAnswerPayload.isCorrect());
        answer.setText(createAnswerPayload.getText());
        answer.setImgUrl(createAnswerPayload.getImgUrl());
        questionRepository.save(question);
        answerRepository.save(answer);
        return modelMapper.fromAnswerToAnswerDto(answer);
    }

    public QuizDto deleteQuestion(Long quizId, Long id) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new NullPointerException("Quiz does not exist"));
        Question question = questionRepository.findById(id).orElseThrow(() -> new NullPointerException("Question does not exist"));
        questionRepository.delete(question);
        return modelMapper.fromQuizToQuizDto(quiz);
    }

    public List<UserQuizDto> getAllUserQuizzes(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NullPointerException("User does not exist"));
        return user.getUserQuizzes().stream().map(modelMapper::fromUserQuizToUserQuizDto).collect(Collectors.toList());
    }

    public QuizDto joinToTheQuiz(Long userId, Long quizId) {
        if (userQuizRepository.findByUserIdAndQuizId(userId, quizId).isPresent()) {
            return null;
        }
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new NullPointerException("Quiz does not exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User does not exist"));
        UserQuiz userQuiz = UserQuiz.builder()
                .user(user)
                .quiz(quiz)
                .score(0)
                .build();
        userQuizRepository.save(userQuiz);
        quiz.getQuestions().forEach(x -> userAnswerRepository.saveAndFlush(UserAnswer.builder()
                .question(x)
                .answer(null)
                .userQuiz(userQuiz)
                .build()));
        return modelMapper.fromQuizToQuizDto(quiz);
    }


    public UserAnswerDto voteOnAnswer(Long userId, Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new NullPointerException("Answer does not exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User does not exist"));
        UserAnswer userAnswer = userAnswerRepository.findByQuestionIdAndUserQuizId(answer.getQuestion().getId(),user.getUserQuizzes().stream().filter(x -> x.getQuiz().equals(answer.getQuestion().getQuiz())).findFirst().get().getId()).orElseThrow(() -> new NullPointerException("User Answer not init"));
        userAnswer.setAnswer(answer);
        userAnswerRepository.saveAndFlush(userAnswer);
        if (answer.isCorrect()) {
            UserQuiz userQuiz = user.getUserQuizzes().stream().filter(x -> x.getQuiz().equals(answer.getQuestion().getQuiz())).findFirst().orElseThrow(() -> new NullPointerException("Vote on answer -> answer is correct exception."));
            userQuiz.setScore(userQuiz.getScore() + 1);
            userQuizRepository.saveAndFlush(userQuiz);
        }
        return modelMapper.formUserAnswerToUserAnswerDto(userAnswer);
    }
}