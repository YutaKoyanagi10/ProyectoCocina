package org.proyectococinav2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.repository.RecipeIngredientRepository;
import org.proyectococinav2.service.impl.RecipeIngredientServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientServiceImplTest {

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @InjectMocks
    private RecipeIngredientServiceImpl recipeIngredientService;

    @Test
    void save_updatesRecipeIngredientIfExists() {
        RecipeIngredient ri = mock(RecipeIngredient.class);
        when(ri.getRecipeId()).thenReturn(1L);
        when(ri.getIngredientId()).thenReturn(2L);
        when(recipeIngredientRepository.existsById(1L, 2L)).thenReturn(true);

        recipeIngredientService.save(ri);

        verify(recipeIngredientRepository).update(ri);
        verify(recipeIngredientRepository, never()).save(ri);
    }

    @Test
    void save_savesRecipeIngredientIfNotExists() {
        RecipeIngredient ri = mock(RecipeIngredient.class);
        when(ri.getRecipeId()).thenReturn(1L);
        when(ri.getIngredientId()).thenReturn(2L);
        when(recipeIngredientRepository.existsById(1L, 2L)).thenReturn(false);

        recipeIngredientService.save(ri);

        verify(recipeIngredientRepository).save(ri);
        verify(recipeIngredientRepository, never()).update(ri);
    }

    @Test
    void deleteById_deletesIfExists() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        when(recipeIngredientRepository.existsById(recipeId, ingredientId)).thenReturn(true);

        recipeIngredientService.deleteById(recipeId, ingredientId);

        verify(recipeIngredientRepository).deleteById(recipeId, ingredientId);
    }

    @Test
    void deleteById_throwsExceptionIfNotExists() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        when(recipeIngredientRepository.existsById(recipeId, ingredientId)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.deleteById(recipeId, ingredientId));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void existsById_returnsTrueIfExists() {
        when(recipeIngredientRepository.existsById(1L, 2L)).thenReturn(true);

        assertTrue(recipeIngredientService.existsById(1L, 2L));
    }

    @Test
    void existsById_returnsFalseIfNotExists() {
        when(recipeIngredientRepository.existsById(1L, 2L)).thenReturn(false);

        assertFalse(recipeIngredientService.existsById(1L, 2L));
    }

    @Test
    void existsById_throwsExceptionIfIdIsNull() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.existsById(null, 2L));
        assertEquals("Recipe ID and Ingredient ID cannot be null.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.existsById(1L, null));
        assertEquals("Recipe ID and Ingredient ID cannot be null.", ex2.getMessage());
    }

    @Test
    void findById_returnsRecipeIngredientIfExists() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        RecipeIngredient ri = mock(RecipeIngredient.class);
        when(recipeIngredientRepository.findById(recipeId, ingredientId)).thenReturn(Optional.of(ri));

        Optional<RecipeIngredient> result = recipeIngredientService.findById(recipeId, ingredientId);

        assertTrue(result.isPresent());
        assertEquals(ri, result.get());
    }

    @Test
    void findById_throwsExceptionIfNotExists() {
        Long recipeId = 1L;
        Long ingredientId = 2L;
        when(recipeIngredientRepository.findById(recipeId, ingredientId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.findById(recipeId, ingredientId));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void findAll_returnsListOfRecipeIngredients() {
        RecipeIngredient ri1 = mock(RecipeIngredient.class);
        RecipeIngredient ri2 = mock(RecipeIngredient.class);
        List<RecipeIngredient> list = List.of(ri1, ri2);
        when(recipeIngredientRepository.findAll()).thenReturn(list);

        List<RecipeIngredient> result = recipeIngredientService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(ri1));
        assertTrue(result.contains(ri2));
    }

    @Test
    void findAllByRecipeId_returnsListIfRecipeExists() {
        Long recipeId = 1L;
        RecipeIngredient ri = mock(RecipeIngredient.class);
        List<RecipeIngredient> list = List.of(ri);
        when(recipeIngredientRepository.findAllByRecipeId(recipeId)).thenReturn(list);

        List<RecipeIngredient> result = recipeIngredientService.findAllByRecipeId(recipeId);

        assertEquals(1, result.size());
        assertEquals(ri, result.getFirst());
    }

    @Test
    void findAllByRecipeId_throwsExceptionIfRecipeIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.findAllByRecipeId(null));
        assertEquals("Recipe ID cannot be null.", ex.getMessage());
    }

    @Test
    void findAllByIngredientId_returnsListIfIngredientExists() {
        Long ingredientId = 2L;
        RecipeIngredient ri = mock(RecipeIngredient.class);
        List<RecipeIngredient> list = List.of(ri);
        when(recipeIngredientRepository.findAllByIngredientId(ingredientId)).thenReturn(list);

        List<RecipeIngredient> result = recipeIngredientService.findAllByIngredientId(ingredientId);

        assertEquals(1, result.size());
        assertEquals(ri, result.getFirst());
    }

    @Test
    void findAllByIngredientId_throwsExceptionIfIngredientIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> recipeIngredientService.findAllByIngredientId(null));
        assertEquals("Ingredient ID cannot be null.", ex.getMessage());
    }
}