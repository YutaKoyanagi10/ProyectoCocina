package org.proyectococina.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococina.domain.dto.RecipeDTO;
import org.proyectococina.domain.dto.RecipeIngredientDTO;
import org.proyectococina.domain.model.Ingredient;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.domain.model.Recipe;
import org.proyectococina.repository.IngredientRepository;
import org.proyectococina.repository.RecipeIngredientRepository;
import org.proyectococina.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock private RecipeRepository recipeRepo;
    @Mock private RecipeIngredientRepository riRepo;
    @Mock private IngredientRepository ingredientRepo;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeService(recipeRepo, riRepo, ingredientRepo);
    }

    @Test
    void save_ShouldThrowException_WhenIngredientDoesNotExist() {
        // GIVEN 
        String ingredientName = "Null Ingredient";
        RecipeIngredientDTO ingDTO = new RecipeIngredientDTO(ingredientName, 10.0, MeasurementUnit.KILOGRAMO);
        RecipeDTO recipeDTO = new RecipeDTO(null, "Recipe Test", "Steps...", List.of(ingDTO), null, null);

        Recipe mockSavedRecipe = new Recipe();
        mockSavedRecipe.setId(1L);
        when(recipeRepo.save(any(Recipe.class))).thenReturn(mockSavedRecipe);

        when(ingredientRepo.findByName(ingredientName)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recipeService.save(recipeDTO);
        });

        assertTrue(exception.getMessage().contains("Ingredient does not exist"));
    }

    @Test
    void save_ShouldCallUpdate_WhenIdIsNotNull() {
        // GIVEN
        RecipeDTO dto = new RecipeDTO(10L, "Update Test", "Steps", List.of(), null, null);
        Recipe mockEntity = new Recipe();
        mockEntity.setId(10L);

        when(recipeRepo.update(any(Recipe.class))).thenReturn(mockEntity);

        // WHEN
        recipeService.save(dto);

        // THEN
        verify(recipeRepo).update(any(Recipe.class));
        verify(riRepo).deleteAllByRecipeId(10L);
        verify(recipeRepo, never()).save(any());
    }

    @Test
    void save_ShouldPersistRecipeAndIngredients_WhenDataIsValid() {
        // GIVEN
        String ingName = "Harina";
        RecipeIngredientDTO ingDTO = new RecipeIngredientDTO(ingName, 500.0, MeasurementUnit.GRAMO);
        RecipeDTO recipeDTO = new RecipeDTO(null, "Pizza", "Mezclar y hornear", List.of(ingDTO), null, null);

        // Mock de la receta guardada
        Recipe mockSavedRecipe = new Recipe();
        mockSavedRecipe.setId(100L);
        when(recipeRepo.save(any(Recipe.class))).thenReturn(mockSavedRecipe);

        // Mock del ingrediente existente
        org.proyectococina.domain.model.Ingredient mockIng = new org.proyectococina.domain.model.Ingredient();
        mockIng.setId(50L);
        mockIng.setName(ingName);
        when(ingredientRepo.findByName(ingName)).thenReturn(Optional.of(mockIng));

        // WHEN
        recipeService.save(recipeDTO);

        // THEN
        verify(recipeRepo).save(any(Recipe.class));
        verify(riRepo, times(1)).save(any());
        assertDoesNotThrow(() -> {
        });
    }
    
    @Test
    void save_ShouldPersistOnlyRecipe_WhenIngredientListIsEmpty() {
        // GIVEN
        RecipeDTO dto = new RecipeDTO(null, "Receta Vacía", "Pasos", List.of(), null, null);
        Recipe mockSaved = new Recipe();
        mockSaved.setId(200L);
        when(recipeRepo.save(any())).thenReturn(mockSaved);

        // WHEN
        recipeService.save(dto);

        // THEN
        verify(recipeRepo).save(any());
        verifyNoInteractions(riRepo);
    }

    @Test
    void save_ShouldRefreshIngredients_WhenUpdatingRecipe() {
        // GIVEN
        RecipeIngredientDTO ing = new RecipeIngredientDTO("Agua", 1.0, MeasurementUnit.LITRO);
        RecipeDTO dto = new RecipeDTO(1L, "Sopa", "Hervir", List.of(ing), null, null);

        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(1L);
        when(recipeRepo.update(any())).thenReturn(mockRecipe);

        Ingredient mockIng = new org.proyectococina.domain.model.Ingredient();
        mockIng.setId(10L);
        when(ingredientRepo.findByName("Agua")).thenReturn(Optional.of(mockIng));

        // WHEN
        recipeService.save(dto);

        // THEN
        verify(riRepo).deleteAllByRecipeId(1L); 
        verify(riRepo).save(any()); 
    }
}