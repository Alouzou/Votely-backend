package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.services.Impl.AuthService;
import com.alouzou.sondage.services.Impl.ChoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChoiceServiceImplTest {
    @Mock
    private ChoiceRepository choiceRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private ChoiceServiceImpl choiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddChoice_AdminUser_Success() {
        // Préparation des données
        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setChoiceText("Option A");

        Question question = new Question();
        Survey survey = new Survey();
        User creator = new User();
        creator.setId(1L);
        survey.setCreator(creator);
        question.setSurvey(survey);

        User adminUser = new User();
        adminUser.setId(2L);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(authService.getCurrentUser()).thenReturn(adminUser);
        when(authService.hasRole(adminUser, RoleName.ROLE_ADMIN)).thenReturn(true);

        Choice savedChoice = new Choice();
        savedChoice.setId(100L);
        savedChoice.setChoiceText("Option A");
        when(choiceRepository.save(any(Choice.class))).thenReturn(savedChoice);


        Choice result = choiceService.addChoice(choiceDTO, 1L);


        assertNotNull(result);
        assertEquals("Option A", result.getChoiceText());
        verify(choiceRepository).save(any(Choice.class));
    }

    @Test
    void testAddChoice_CreatorUser_Success() {

        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setChoiceText("Option B");

        User creator = new User();
        creator.setId(1L);

        Survey survey = new Survey();
        survey.setCreator(creator);

        Question question = new Question();
        question.setSurvey(survey);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(authService.getCurrentUser()).thenReturn(creator);
        when(authService.hasRole(creator, RoleName.ROLE_ADMIN)).thenReturn(false);

        Choice choice = new Choice();
        choice.setChoiceText("Option B");
        when(choiceRepository.save(any(Choice.class))).thenReturn(choice);


        Choice result = choiceService.addChoice(choiceDTO, 1L);


        assertNotNull(result);
        assertEquals("Option B", result.getChoiceText());
        verify(choiceRepository).save(any(Choice.class));
    }

    @Test
    void testAddChoice_QuestionNotFound_ThrowsException() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setChoiceText("Non applicable");


        assertThrows(EntityNotFoundException.class,
                () -> choiceService.addChoice(choiceDTO, 99L));
    }

    @Test
    void testAddChoice_UnauthorizedUser_ThrowsForbiddenActionException() {
        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setChoiceText("Option Interdite");

        User creator = new User();
        creator.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Survey survey = new Survey();
        survey.setCreator(creator);

        Question question = new Question();
        question.setSurvey(survey);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(authService.getCurrentUser()).thenReturn(otherUser);
        when(authService.hasRole(otherUser, RoleName.ROLE_ADMIN)).thenReturn(false);

        assertThrows(ForbiddenActionException.class,
                () -> choiceService.addChoice(choiceDTO, 1L));
    }

}
