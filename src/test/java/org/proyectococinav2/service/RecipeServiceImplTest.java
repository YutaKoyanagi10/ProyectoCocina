package org.proyectococinav2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.repository.RecipeRepository;
import org.proyectococinav2.service.impl.RecipeServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void save_shouldUpdateIfExists() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recipeRepository.existsById(1L)).thenReturn(true);

        recipeService.save(recipe);

        verify(recipeRepository).update(recipe);
        verify(recipeRepository, never()).save(recipe);
    }

    @Test
    void save_shouldSaveIfNotExists() {
        Recipe recipe = new Recipe();
        recipe.setId(2L);
        when(recipeRepository.existsById(2L)).thenReturn(false);

        recipeService.save(recipe);

        verify(recipeRepository).save(recipe);
        verify(recipeRepository, never()).update(recipe);
    }

    @Test
    void deleteById_shouldDeleteIfExists() {
        when(recipeRepository.existsById(1L)).thenReturn(true);

        recipeService.deleteById(1L);

        verify(recipeRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowIfNotExists() {
        when(recipeRepository.existsById(1L)).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.deleteById(1L));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(recipeRepository.existsById(1L)).thenReturn(true);

        assertTrue(recipeService.existsById(1L));
    }

    @Test
    void existsById_shouldThrowIfNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.existsById(null));
        assertEquals("ID cannot be null.", ex.getMessage());
    }

    @Test
    void findById_shouldReturnRecipe() {
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(recipe, result.get());
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.findById(1L));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void findAll_shouldReturnList() {
        Recipe r1 = new Recipe();
        Recipe r2 = new Recipe();
        List<Recipe> recipes = Arrays.asList(r1, r2);
        when(recipeRepository.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findRecipeByName_shouldReturnRecipe() {
        Recipe recipe = new Recipe();
        when(recipeRepository.findRecipeByName("Tarta")).thenReturn(Optional.of(recipe));

        Optional<Recipe> result = recipeService.findRecipeByName("Tarta");

        assertTrue(result.isPresent());
        assertEquals(recipe, result.get());
    }

    @Test
    void findRecipeByName_shouldThrowIfNotFound() {
        when(recipeRepository.findRecipeByName("Tarta")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.findRecipeByName("Tarta"));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void findRecipeIdByName_shouldReturnId() {
        when(recipeRepository.findRecipeIdByName("Tarta")).thenReturn(Optional.of(10L));

        Optional<Long> result = recipeService.findRecipeIdByName("Tarta");

        assertTrue(result.isPresent());
        assertEquals(Optional.of(10L), result);
    }

    @Test
    void findRecipeIdByName_shouldThrowIfNotFound() {
        when(recipeRepository.findRecipeIdByName("Tarta")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> recipeService.findRecipeIdByName("Tarta"));
        assertTrue(ex.getMessage().contains("does not exist"));
    }
}