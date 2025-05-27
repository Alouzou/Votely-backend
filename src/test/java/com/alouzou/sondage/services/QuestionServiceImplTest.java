package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.Impl.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuestion_Success() {

        ChoiceDTO choice1 = new ChoiceDTO();
        choice1.setChoiceText("Choix A");

        ChoiceDTO choice2 = new ChoiceDTO();
        choice2.setChoiceText("Choix B");

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionText("Quelle est votre couleur préférée ?");
        questionDTO.setSurveyId(1L);
        questionDTO.setChoices(Arrays.asList(choice1, choice2));

        Survey survey = new Survey();
        survey.setId(1L);

        Question savedQuestion = new Question();
        savedQuestion.setId(1L);
        savedQuestion.setSurvey(survey);
        savedQuestion.setQuestionText(questionDTO.getQuestionText());

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);


        Question result = questionService.createQuestion(questionDTO);


        assertNotNull(result);
        assertEquals("Quelle est votre couleur préférée ?", result.getQuestionText());
        verify(surveyRepository).findById(1L);
        verify(questionRepository, times(2)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_SurveyNotFound_ThrowsException() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setSurveyId(99L);

        when(surveyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            questionService.createQuestion(questionDTO);
        });

        verify(surveyRepository).findById(99L);
        verify(questionRepository, never()).save(any());
    }
}
