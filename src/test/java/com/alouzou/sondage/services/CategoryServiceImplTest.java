package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.exceptions.CategoryInactiveException;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.services.Impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Test
    void createCategory_nomValide_retourneCategorie() {
        String name = "Sport";
        Category expected = new Category(name, true);

        when(categoryRepository.findByName(name)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(expected);

        Category result = categoryService.createCategory(name, true);

        assertEquals(expected, result);
    }

    @Test
    void createCategory_nomVide_lanceIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory("  ", true));
    }

    @Test
    void createCategory_nomDejaPris_lanceResourceAlreadyUsedException() {
        when(categoryRepository.findByName("Culture")).thenReturn(Optional.of(new Category()));

        assertThrows(ResourceAlreadyUsedException.class, () -> categoryService.createCategory("Culture", true));
    }

    @Test
    void modifyCategory_valide_modifieEtRetourneCategorie() {
        Long id = 1L;
        Category existante = new Category("Ancien", true);
        existante.setId(id);

        Category modif = new Category("Nouvelle", false);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existante));
        when(categoryRepository.findByName("Nouvelle")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(existante);

        Category result = categoryService.modifyCategory(id, modif);

        assertEquals("Nouvelle", result.getName());
        assertFalse(result.isActive());
    }

    @Test
    void modifyCategory_categorieNonTrouvee_lanceEntityNotFoundException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.modifyCategory(99L, new Category()));
    }

    @Test
    void modifyCategory_categorieDesactivee_lanceCategoryInactiveException() {
        Category cat = new Category("Inactif", false);
        cat.setId(10L);
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(cat));

        assertThrows(CategoryInactiveException.class, () -> categoryService.modifyCategory(10L, new Category()));
    }

    @Test
    void modifyCategory_nomVide_lanceIllegalArgumentException() {
        Category existante = new Category("Valide", true);
        existante.setId(1L);
        Category modif = new Category("", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existante));

        assertThrows(IllegalArgumentException.class, () -> categoryService.modifyCategory(1L, modif));
    }

    @Test
    void modifyCategory_nomPris_lanceResourceAlreadyUsedException() {
        Category existante = new Category("Ancien", true);
        existante.setId(1L);

        Category modif = new Category("DéjàPris", true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existante));
        when(categoryRepository.findByName("DéjàPris")).thenReturn(Optional.of(new Category()));

        assertThrows(ResourceAlreadyUsedException.class, () -> categoryService.modifyCategory(1L, modif));
    }

    @Test
    void deleteById_categorieSansSondage_estSupprimée() {
        Category cat = new Category("À supprimer", true);
        cat.setId(5L);

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(cat));
        when(surveyRepository.findByCategory_Id(5L)).thenReturn(List.of());

        categoryService.deleteById(5L);

        verify(categoryRepository).deleteById(5L);
    }

    @Test
    void deleteById_categorieAvecSondage_estDesactivéeEtException() {
        Category cat = new Category("Utilisée", true);
        cat.setId(6L);

        when(categoryRepository.findById(6L)).thenReturn(Optional.of(cat));
        Survey survey = new Survey();
        when(surveyRepository.findByCategory_Id(6L)).thenReturn(List.of(survey)); // sondage fictif

        ResourceAlreadyUsedException exception = assertThrows(ResourceAlreadyUsedException.class, () -> categoryService.deleteById(6L));

        assertEquals("La catégorie avec l'id 6 est dèja utilisée dans d'autres sondages.\nLa catégorie a été désactivée", exception.getMessage());
        verify(categoryRepository).save(argThat(c -> !c.isActive()));
    }

    @Test
    void deleteById_categorieInexistante_lanceEntityNotFoundException() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(100L));
    }

    @Test
    void findAllByIsActive_retourneListe() {
        List<Category> liste = List.of(new Category("Sport", true));
        when(categoryRepository.findAllByIsActiveTrue()).thenReturn(Optional.of(liste));

        List<Category> result = categoryService.findAllByIsActive();

        assertEquals(1, result.size());
    }

    @Test
    void findAllByIsActive_aucuneCategorie_lanceException() {
        when(categoryRepository.findAllByIsActiveTrue()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findAllByIsActive());
    }

    @Test
    void findByIdAndIsActiveTrue_retourneCategorie() {
        Category cat = new Category("Active", true);
        when(categoryRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(cat));

        Category result = categoryService.findByIdAndIsActiveTrue(1L);

        assertEquals(cat, result);
    }

    @Test
    void findByIdAndIsActiveTrue_pasTrouvee_lanceEntityNotFoundException() {
        when(categoryRepository.findByIdAndIsActiveTrue(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findByIdAndIsActiveTrue(999L));
    }







}
