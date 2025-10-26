package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.repositories.VoteRepository;
import com.alouzou.sondage.services.Impl.AuthService;
import com.alouzou.sondage.services.Impl.VoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceImplTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChoiceRepository choiceRepository;

    @Mock
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitVote_Success() {

        User user = new User();
        user.setId(1L);

        Survey survey = new Survey();
        survey.setId(10L);

        Question question = new Question();
        question.setSurvey(survey);

        Choice choice = new Choice();
        choice.setId(5L);
        choice.setQuestion(question);

        VoteDTO dto = new VoteDTO();
        dto.setChoiceId(5L);

        when(authService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(choiceRepository.findById(5L)).thenReturn(Optional.of(choice));

        Vote savedVote = Vote.builder().user(user).choice(choice).build();
        when(voteRepository.save(any(Vote.class))).thenReturn(savedVote);


        Vote result = voteService.submitVote(dto);


        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(choice, result.getChoice());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    void testSubmitVote_UserNotFound_ThrowsException() {
        User principal = new User();
        principal.setId(2L);

        when(authService.getCurrentUser()).thenReturn(principal);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        VoteDTO dto = new VoteDTO();
        dto.setChoiceId(5L);

        assertThrows(EntityNotFoundException.class, () -> voteService.submitVote(dto));
    }

    @Test
    void testSubmitVote_ChoiceNotFound_ThrowsException() {
        User user = new User();
        user.setId(1L);

        when(authService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(choiceRepository.findById(5L)).thenReturn(Optional.empty());

        VoteDTO dto = new VoteDTO();
        dto.setChoiceId(5L);

        assertThrows(EntityNotFoundException.class, () -> voteService.submitVote(dto));
    }

    @Test
    void testSubmitVote_AlreadyVotedForChoice_ThrowsException() {
        User user = new User();
        user.setId(1L);

        Choice choice = new Choice();
        choice.setId(5L);

        when(authService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(choiceRepository.findById(5L)).thenReturn(Optional.of(choice));

        VoteDTO dto = new VoteDTO();
        dto.setChoiceId(5L);

        assertThrows(IllegalArgumentException.class, () -> voteService.submitVote(dto));
    }

    @Test
    void testSubmitVote_AlreadyVotedForSurvey_ThrowsException() {
        User user = new User();
        user.setId(1L);

        Survey survey = new Survey();
        survey.setId(10L);

        Question question = new Question();
        question.setSurvey(survey);

        Choice choice = new Choice();
        choice.setId(5L);
        choice.setQuestion(question);

        VoteDTO dto = new VoteDTO();
        dto.setChoiceId(5L);

        when(authService.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(choiceRepository.findById(5L)).thenReturn(Optional.of(choice));
        assertThrows(ForbiddenActionException.class, () -> voteService.submitVote(dto));
    }

//    @Test
//    void testHasUserAlreadyVotedForSurvey_True() {
//        Choice choice = new Choice();
//        Survey survey = new Survey();
//        survey.setId(99L);
//        Question question = new Question();
//        question.setSurvey(survey);
//        choice.setQuestion(question);
//
//        when(choiceRepository.findById(7L)).thenReturn(Optional.of(choice));
//        assertTrue(result);
//    }

//    @Test
//    void testHasUserAlreadyVotedForSurvey_ChoiceNotFound() {
//        when(choiceRepository.findById(7L)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class,
//                () -> voteService.hasUserAlreadyVotedForSurvey(7L, 3L));
//    }
}
