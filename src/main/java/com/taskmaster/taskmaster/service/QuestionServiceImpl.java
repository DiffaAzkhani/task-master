package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.mapper.QuestionMapper;
import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.response.GetQuestionResponse;
import com.taskmaster.taskmaster.model.response.QuestionResponse;
import com.taskmaster.taskmaster.repository.AnswerRepository;
import com.taskmaster.taskmaster.repository.QuestionRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    private final QuestionMapper questionMapper;

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

        return Collections.singletonList(questionMapper.toQuestionResponse(question));
    }

    @Override
    public Page<GetQuestionResponse> getQuestionForStudy(Long studyId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Question> questionList = questionRepository.findByStudyId(studyId);

        Collections.shuffle(questionList);

        return pagingQuestion(pageRequest, questionList);
    }

    private Page<GetQuestionResponse> pagingQuestion(PageRequest pageRequest, List<Question> question) {

        List<GetQuestionResponse> questionResponse = paginateAndConvertToResponse(pageRequest, question);

        return new PageImpl<>(questionResponse, pageRequest, questionResponse.size());
    }

    private List<GetQuestionResponse> paginateAndConvertToResponse(PageRequest pageRequest, List<Question> questionList) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), questionList.size());

        List<Question> pageContent = questionList.subList(start, end);
        return pageContent.stream()
            .map(questionMapper::toGetQuestionResponse)
            .collect(Collectors.toList());
    }

}
