package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.entity.UserAnswer;
import com.taskmaster.taskmaster.entity.UserGrade;
import com.taskmaster.taskmaster.mapper.QuestionMapper;
import com.taskmaster.taskmaster.mapper.UserGradeMapper;
import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.request.UpdateAnswerRequest;
import com.taskmaster.taskmaster.model.request.UpdateQuestionsRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserAnswerRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsAdminResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsUserResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import com.taskmaster.taskmaster.model.response.UpdateQuestionsResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserAnswerResponse;
import com.taskmaster.taskmaster.repository.AnswerRepository;
import com.taskmaster.taskmaster.repository.QuestionRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserAnswerRepository;
import com.taskmaster.taskmaster.repository.UserGradeRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final StudyRepository studyRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final UserRepository userRepository;

    private final UserAnswerRepository userAnswerRepository;

    private final UserGradeRepository userGradeRepository;

    private final QuestionMapper questionMapper;

    private final UserGradeMapper userGradeMapper;

    private final ValidationService validationService;

    @Override
    @Transactional
    public List<AddQuestionResponse> createQuestionAndAnswer(AddQuestionRequest request) {
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

        return Collections.singletonList(questionMapper.toAddQuestionResponse(question));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetQuestionsUserResponse> getQuestionAndAnswerForUser(Long studyId, int page, int size) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!"));

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        if (!userRepository.existsByUsernameAndStudies(user.getUsername(), study)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't have this study!");
        }

        List<Question> questionList = questionRepository.findByStudyId(studyId);
        Collections.shuffle(questionList);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByStudy(study, pageRequest);

        List<GetQuestionsUserResponse> getQuestionsUserResponse = questionPage.getContent().stream()
            .map(questionMapper::toGetQuestionForUserResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(getQuestionsUserResponse, pageRequest, questionPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetQuestionsAdminResponse> getQuestionAndAnswerForAdmin(Long studyId, int page, int size) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByStudy(study, pageRequest);

        List<GetQuestionsAdminResponse> getQuestionsAdminResponse = questionPage.getContent().stream()
            .map(questionMapper::toGetQuestionForAdminResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(getQuestionsAdminResponse, pageRequest, questionPage.getTotalElements());
    }

    @Override
    @Transactional
    public void answerSubmission(Long studyId, AnswerSubmissionRequest request) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
               log.info("User with username:{}, not found!", currentUser);
               return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.info("Study with id:{}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        if (!studyRepository.existsByUsers(user)) {
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN,"User didn't buy this study");
        }

        Question question = questionRepository.findByIdAndStudy(request.getQuestionId(), study)
            .orElseThrow(() -> {
                log.info("Question ID not found!");
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Question ID not found!");
            });

        Answer answer = answerRepository.findByIdAndQuestion(request.getAnswerId(), question)
            .orElseThrow(() -> {
                log.info("Answer ID not found!");
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Answer ID not found!");
            });

        if (userAnswerRepository.existsByUserAndQuestion(user, question)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User has already answer this question!");
        }

        UserAnswer userAnswer = UserAnswer.builder()
            .user(user)
            .study(study)
            .question(question)
            .answer(answer)
            .build();

        userAnswerRepository.save(userAnswer);
        log.info("Success to save user answer");
    }

    @Override
    @Transactional
    public GradeSubmissionResponse gradeSubmission(Long studyId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.info("Study with id:{}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        UserGrade existingUserGrade = userGradeRepository.findByUser_UsernameAndStudy(currentUser, study);

        if (userGradeRepository.existsByUserAndStudy(user, study)) {
            userGradeRepository.delete(existingUserGrade);
        }

        List<Question> questionList = questionRepository.findByStudy(study);

        if (questionList.isEmpty()) {
            log.info("Study didn't have question!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Study didn't have any question!");
        }

        List<UserAnswer> userAnswerList = userAnswerRepository.findByUserAndQuestionIn(user, questionList);

        if (userAnswerList.isEmpty()) {
            log.info("Question didn't have any answer!");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Question didn't have any answer!");
        }

        int scoreGrade = countUserSubmission(userAnswerList, questionList);

        UserGrade userGrade = UserGrade.builder()
            .user(user)
            .study(study)
            .score(scoreGrade)
            .gradedAt(TimeUtil.getFormattedLocalDateTimeNow())
            .build();

        userGradeRepository.save(userGrade);
        log.info("Success to save user grade!");

        return userGradeMapper.toUserGradeResponse(userGrade);
    }

    @Override
    @Transactional(readOnly = true)
    public GetExplanationResponse getExplanationAndUserAnswer(Long studyId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.info("Study with id:{}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        List<UserAnswer> userAnswerList = userAnswerRepository.findByUser_UsernameAndStudy(currentUser, study);

        if (userAnswerList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User answer is empty!");
        }

        UserGrade userGrade = userGradeRepository.findByUser_UsernameAndStudy(currentUser, study);

        return questionMapper.toGetAllExplanationResponse(userAnswerList ,userGrade);
    }

    @Override
    @Transactional
    public UpdateQuestionsResponse updateQuestionsForAdmin(Long questionId, UpdateQuestionsRequest request) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!"));

        updateQuestionProperties(question, request);

        questionRepository.save(question);

        return questionMapper.toUpdateQuestionResponse(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!"));

        questionRepository.delete(question);
    }

    @Override
    @Transactional
    public void deleteUserAnswer(Long studyId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!"));

        if (!userRepository.existsByUsernameAndStudies(currentUser, study)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user doesn't have this study!");
        }

        List<UserAnswer> userAnswer = userAnswerRepository.findByUser_UsernameAndStudy(currentUser, study);

        if (userAnswer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User didn't have any answer!");
        }

        UserGrade userGrade = userGradeRepository.findByUser_UsernameAndStudy(currentUser, study);

        userGradeRepository.delete(userGrade);

        userAnswerRepository.deleteAll(userAnswer);

    }

    @Override
    @Transactional
    public void deleteUserAnswerForAdmin(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!"));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        List<UserAnswer> userAnswer = userAnswerRepository.findByUser_UsernameAndStudy(user.getUsername(), study);

        if (userAnswer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User didn't have any answer!");
        }

        UserGrade userGrade = userGradeRepository.findByUser_UsernameAndStudy(user.getUsername(), study);
        userGradeRepository.delete(userGrade);

        userAnswerRepository.deleteAll(userAnswer);
    }

    @Override
    @Transactional
    public UpdateUserAnswerResponse updateUserAnswerResponse(List<UpdateUserAnswerRequest> requestList, Long studyId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        List<UserAnswer> userAnswer = userAnswerRepository.findByUser_UsernameAndStudy(currentUser, study);

        requestList.forEach(request -> updateUserAnswerProperties(request, userAnswer));

        userAnswerRepository.saveAll(userAnswer);

        return questionMapper.toUpdateUserAnswerResponse(userAnswer);
    }

    private void updateUserAnswerProperties(UpdateUserAnswerRequest request, List<UserAnswer> userAnswerList) {
        userAnswerList.stream()
            .filter(userAnswer -> Objects.equals(userAnswer.getQuestion().getId(), request.getQuestionId()))
            .findFirst()
            .ifPresent(userAnswer -> {
                if (Objects.nonNull(request.getAnswerId())) {
                    Answer answer = answerRepository.findById(request.getAnswerId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found!"));

                    if (!Objects.equals(answer.getQuestion().getId(), request.getQuestionId())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answer id didnt match with question!");
                    }

                    userAnswer.setAnswer(answer);
                }
            });
    }

    private void updateQuestionProperties(Question question, UpdateQuestionsRequest request) {
        if (Objects.nonNull(request.getQuestionText())) {
            question.setQuestionText(request.getQuestionText());
        }

        if (Objects.nonNull(request.getExplanation())) {
            question.setExplanation(request.getExplanation());
        }

        if (Objects.nonNull(request.getImageUrl())) {
            question.setImageUrl(request.getImageUrl());
        }

        if (Objects.nonNull(request.getAnswers())) {
            List<UpdateAnswerRequest> updateAnswerList = request.getAnswers();

            question.getAnswers().forEach(existingAnswer -> {
                updateAnswerList.stream()
                    .filter(answer -> answer.getAnswerId() != null && answer.getAnswerId().equals(existingAnswer.getId()))
                    .findFirst()
                    .ifPresent(newAnswer -> {
                        if (Objects.nonNull(newAnswer.getAnswerText())) {
                            existingAnswer.setAnswerText(newAnswer.getAnswerText());
                        }
                        if (Objects.nonNull(newAnswer.getIsCorrect())) {
                            existingAnswer.setIsCorrect(newAnswer.getIsCorrect());
                        }
                    });
            });
        }
    }

    private int countUserSubmission(List<UserAnswer> userAnswerList, List<Question> questionList) {
        int totalQuestion = questionList.size();
        int correctAnswers = 0;

        for (UserAnswer userAnswer : userAnswerList) {
            Answer correctAnswer =  answerRepository.findByQuestionAndIsCorrectTrue(userAnswer.getQuestion())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Correct answer not found in user!"));

            if (correctAnswer.getId().equals(userAnswer.getAnswer().getId())) {
                correctAnswers++;
            }
        }

        double score = ((double) correctAnswers / totalQuestion) * 100;

        return (int) score;
    }

}
