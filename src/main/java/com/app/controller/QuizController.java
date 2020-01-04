package com.app.controller;

import com.app.model.dto.*;
import com.app.payloads.requests.CreateAnswerPayload;
import com.app.payloads.requests.CreateQuestionPayload;
import com.app.payloads.requests.CreateQuizPayload;
import com.app.payloads.responses.UserScore;
import com.app.security.CurrentUser;
import com.app.security.CustomUserDetails;
import com.app.service.QuizService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@Api(tags = "Quiz controller")
public class QuizController {
    private QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public QuizDto getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }

    @GetMapping("/user")
    public List<QuizDto> getAllQuizzesForUser(@ApiIgnore @CurrentUser CustomUserDetails userDetails) {
        return quizService.getQuizesForUser(userDetails.getId());
    }

    @GetMapping("/{quizId}/questions")
    public List<QuestionDto> getAllQuestionsForQuiz(@PathVariable Long quizId) {
        return quizService.getAllQuestionsByQuizId(quizId);
    }

    @GetMapping("/{questionId}/answers")
    public List<AnswerDto> getAllAnswersForQuestion(@PathVariable Long questionId) {
        return quizService.getAllAnswersByQuestionId(questionId);
    }

    @GetMapping("/ranking")
    public List<UserScore> getRanking(){
        return quizService.getUsersScore();
    }

    @PostMapping
    public QuizDto createNewQuiz(@RequestBody CreateQuizPayload createQuizPayload) {
        return quizService.createQuiz(createQuizPayload);
    }

    @PutMapping("/{id}")
    public QuizDto updateQuz(@PathVariable Long id, @RequestBody CreateQuizPayload createQuizPayload) {
        return quizService.updateQuiz(id, createQuizPayload);
    }

    @PostMapping("/{quizId}/addQuestion")
    public QuestionDto addQuestionToQuiz(@PathVariable Long quizId, @RequestBody CreateQuestionPayload createQuestionPayload) {
        return quizService.addQuestion(quizId, createQuestionPayload);
    }

    @PostMapping("/{questionId}/addAnswer")
    public AnswerDto addAnswerToQuestion(@PathVariable Long questionId, @RequestBody CreateAnswerPayload createAnswerPayload) {
        System.out.println(createAnswerPayload.isCorrect());
        return quizService.addAnswer(questionId, createAnswerPayload);
    }

    @PutMapping("/{questionId}/updateAnswer/{answerId}")
    public AnswerDto updateAnswer(@PathVariable Long questionId, @PathVariable Long answerId, @RequestBody CreateAnswerPayload createAnswerPayload) {
        return quizService.updateAnswer(questionId, answerId, createAnswerPayload);
    }


    @PostMapping("/changeStatus/{quizId}")
    public QuizDto changeQuizStatus(@PathVariable Long quizId) {
        return quizService.changeStatus(quizId);
    }

    @DeleteMapping("{quizId}/deleteQuestion/{questionId}")
    public QuizDto deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId) {
        return quizService.deleteQuestion(quizId, questionId);
    }

    @PostMapping("/joinQuiz/{quizId}")
    public QuizDto joinToTheQuiz(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @PathVariable Long quizId) {
        return quizService.joinToTheQuiz(userDetails.getId(), quizId);
    }

    @PostMapping("/voteOnAnswer/{answerId}")
    public UserAnswerDto voteOnAnswer(@ApiIgnore @CurrentUser CustomUserDetails userDetails, @PathVariable Long answerId) {
        return quizService.voteOnAnswer(userDetails.getId(), answerId);
    }

}
