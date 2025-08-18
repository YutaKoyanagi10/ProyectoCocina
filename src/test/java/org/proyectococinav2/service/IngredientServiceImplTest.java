package org.proyectococinav2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.repository.IngredientRepository;
import org.proyectococinav2.service.impl.IngredientServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceImplTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @ParameterizedTest
    @CsvSource({
            "1, true",
            "2, false"
    })
    void existsById_returnsCorrectValue(Long id, boolean exists) {
        when(ingredientRepository.existsById(id)).thenReturn(exists);

        boolean result = ingredientService.existsById(id);

        assertEquals(exists, result);
    }

    @Test
    void findIngredientByName_returnsIngredientIfExists() {
        String name = "Tomate";
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        when(ingredientRepository.findIngredientByName(name)).thenReturn(Optional.of(ingredient));

        Optional<Ingredient> result = ingredientService.findIngredientByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    void findIngredientByName_throwsExceptionIfNotExists() {
        String name = "NonExistent";
        when(ingredientRepository.findIngredientByName(name)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ingredientService.findIngredientByName(name));
    }

    @Test
    void findIngredientIdByName_returnsIdIfExists() {
        String name = "Tomate";
        Long id = 1L;
        when(ingredientRepository.findIngredientIdByName(name)).thenReturn(Optional.of(id));

        Optional<Long> result = ingredientService.findIngredientIdByName(name);

        assertTrue(result.isPresent());
        assertEquals(id, result.get());
    }

    @Test
    void findIngredientIdByName_throwsExceptionIfNotExists() {
        String name = "NonExistent";
        when(ingredientRepository.findIngredientIdByName(name)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ingredientService.findIngredientIdByName(name));
    }

    @Test
    void existsById_throwsExceptionIfIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ingredientService.existsById(null));
    }

    @Test
    void findAllBySupplierId_returnsIngredientsIfSupplierExists() {
        Long supplierId = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        List<Ingredient> ingredients = List.of(ingredient);
        when(ingredientRepository.findAllBySupplierId(supplierId)).thenReturn(ingredients);

        List<Ingredient> result = ingredientService.findAllBySupplierId(supplierId);

        assertEquals(1, result.size());
        assertEquals(ingredient, result.getFirst());
    }

    @Test
    void findAllBySupplierId_throwsExceptionIfSupplierIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ingredientService.findAllBySupplierId(null));
    }
    @Test
    void findById_returnsIngredientIfExists() {
        Long id = 1L;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        Optional<Ingredient> result = ingredientService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void findById_throwsExceptionIfNotExists() {
        Long id = 99L;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ingredientService.findById(id));
    }

    @Test
    void findAll_returnsListOfIngredients() {
        Ingredient i1 = new Ingredient();
        i1.setId(1L);
        Ingredient i2 = new Ingredient();
        i2.setId(2L);
        List<Ingredient> ingredients = List.of(i1, i2);
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        List<Ingredient> result = ingredientService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(i1));
        assertTrue(result.contains(i2));
    }

    @Test
    void save_updatesIngredientIfExists() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        when(ingredientRepository.existsById(1L)).thenReturn(true);

        ingredientService.save(ingredient);

        verify(ingredientRepository).update(ingredient);
        verify(ingredientRepository, never()).save(ingredient);
    }

    @Test
    void save_savesIngredientIfNotExists() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        when(ingredientRepository.existsById(2L)).thenReturn(false);

        ingredientService.save(ingredient);

        verify(ingredientRepository).save(ingredient);
        verify(ingredientRepository, never()).update(ingredient);
    }

    @Test
    void deleteById_deletesIfExists() {
        Long id = 1L;
        when(ingredientRepository.existsById(id)).thenReturn(true);

        ingredientService.deleteById(id);

        verify(ingredientRepository).deleteById(id);
    }

    @Test
    void deleteById_throwsExceptionIfNotExists() {
        Long id = 99L;
        when(ingredientRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> ingredientService.deleteById(id));
    }
}
