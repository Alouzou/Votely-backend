package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.SurveyDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.Impl.SurveyServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceImplTest {

    @InjectMocks
    private SurveyServiceImpl surveyService;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSurvey_Success() {
        SurveyDTO dto = new SurveyDTO();
        dto.setTitle("Mon Sondage");
        dto.setCreatorId(1L);
        dto.setCategoryId(2L);

        User creator = new User();
        creator.setId(1L);
        creator.setEmail("test@user.com");

        Category category = new Category();
        category.setId(2L);

        Survey savedSurvey = new Survey();
        savedSurvey.setTitle("Mon Sondage");

        when(surveyRepository.findByTitle("Mon Sondage")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(surveyRepository.save(any(Survey.class))).thenReturn(savedSurvey);

        Survey result = surveyService.createSurvey(dto);

        assertNotNull(result);
        assertEquals("Mon Sondage", result.getTitle());
        verify(surveyRepository).save(any(Survey.class));
    }

    @Test
    void testCreateSurvey_TitleAlreadyUsed_ThrowsException() {
        SurveyDTO dto = new SurveyDTO();
        dto.setTitle("Déjà pris");

        when(surveyRepository.findByTitle("Déjà pris"))
                .thenReturn(Optional.of(new Survey()));

        assertThrows(ResourceAlreadyUsedException.class, () -> surveyService.createSurvey(dto));
    }

    @Test
    void testCreateSurvey_UserNotFound_ThrowsException() {
        SurveyDTO dto = new SurveyDTO();
        dto.setTitle("Nouveau sondage");
        dto.setCreatorId(1L);
        dto.setCategoryId(2L);

        when(surveyRepository.findByTitle("Nouveau sondage")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.createSurvey(dto));
    }

    @Test
    void testCreateSurvey_CategoryNotFound_ThrowsException() {
        SurveyDTO dto = new SurveyDTO();
        dto.setTitle("Sondage sans catégorie");
        dto.setCreatorId(1L);
        dto.setCategoryId(99L);

        User user = new User();
        user.setId(1L);

        when(surveyRepository.findByTitle(dto.getTitle())).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.createSurvey(dto));
    }

    @Test
    void testGetSurveysByCategory_Success() {
        Category category = new Category();
        category.setId(2L);

        List<Survey> surveys = Arrays.asList(new Survey(), new Survey());

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(surveyRepository.findByCategory(category)).thenReturn(surveys);

        List<Survey> result = surveyService.getSurveysByCategory(2L);

        assertEquals(2, result.size());
    }

    @Test
    void testGetSurveysByCategory_CategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.getSurveysByCategory(99L));
    }

    @Test
    void testGetSurveysByCategory_EmptySurveyList_ThrowsException() {
        Category category = new Category();
        category.setId(2L);

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(surveyRepository.findByCategory(category)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> surveyService.getSurveysByCategory(2L));
    }

    @Test
    void testGetSurveysByCreator_Success() {
        User creator = new User();
        creator.setId(1L);

        List<Survey> surveys = Arrays.asList(new Survey(), new Survey());

        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(surveyRepository.findByCreator(creator)).thenReturn(surveys);

        List<Survey> result = surveyService.getSurveysByCreator(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testGetSurveysByCreator_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.getSurveysByCreator(1L));
    }

    @Test
    void testGetSurveyById_Success() {
        Survey survey = new Survey();
        survey.setId(1L);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        Optional<Survey> result = surveyService.getSurveyById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetSurveyById_NotFound() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.getSurveyById(1L));
    }

    @Test
    void testFindAll_ReturnsAllSurveys() {
        List<Survey> surveys = Arrays.asList(new Survey(), new Survey(), new Survey());

        when(surveyRepository.findAll()).thenReturn(surveys);

        List<Survey> result = surveyService.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void testDeleteSurvey_Success() {
        Survey survey = new Survey();
        survey.setId(1L);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        surveyService.deleteSurvey(1L);

        verify(surveyRepository).deleteById(1L);
    }

    @Test
    void testDeleteSurvey_NotFound() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> surveyService.deleteSurvey(1L));
    }
}
