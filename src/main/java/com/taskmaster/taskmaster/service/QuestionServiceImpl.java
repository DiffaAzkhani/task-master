package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.response.AnswerResponse;
import com.taskmaster.taskmaster.model.response.QuestionResponse;
import com.taskmaster.taskmaster.repository.AnswerRepository;
import com.taskmaster.taskmaster.repository.QuestionRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final StudyRepository studyRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    @Override
    public List<QuestionResponse> addQuestionAndAnswer(AddQuestionRequest request) {
        Study study = studyRepository.findById(request.getStudyId())
            .orElseThrow(() -> {
               log.warn("Study with code : {}, not found!", request.getStudyId());
               return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        log.info("found study : {}", study);

        Question question = Question.builder()
            .study(study)
            .questionText(request.getQuestionText())
            .imageUrl(request.getImageUrl())
            .explanation(request.getExplanation())
            .build();

        questionRepository.save(question);
        log.info("success to save question!");

        List<Answer> answerList = request.getAnswers().stream()
            .map(addAnswerRequest -> Answer.builder()
                .answerText(addAnswerRequest.getAnswerText())
                .isCorrect(addAnswerRequest.getIsCorrect())
                .question(question)
                .build())
            .collect(Collectors.toList());

        answerRepository.saveAll(answerList);
        log.info("success to save all answer!");

        question.setAnswers(answerList);
        log.info("success to set answer list in question");

        return Collections.singletonList(toQuestionResponse(question));
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.builder()
            .studyId(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .explanation(question.getExplanation())
            .answers(question.getAnswers().stream()
                .map(this::toAnswerResponse)
                .collect(Collectors.toList()))
            .build();
    }

    private AnswerResponse toAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
            .studyId(answer.getId())
            .answerText(answer.getAnswerText())
            .isCorrect(answer.getIsCorrect())
            .build();
    }
}
