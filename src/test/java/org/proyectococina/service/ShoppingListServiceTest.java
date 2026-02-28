package org.proyectococina.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.domain.dto.ShoppingItemDTO;
import org.proyectococina.domain.model.Ingredient;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.domain.model.RecipeIngredient;
import org.proyectococina.domain.model.Supplier;
import org.proyectococina.repository.IngredientRepository;
import org.proyectococina.repository.RecipeIngredientRepository;
import org.proyectococina.repository.SupplierRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock private RecipeIngredientRepository riRepo;
    @Mock private IngredientRepository ingredientRepo;
    @Mock private SupplierRepository supplierRepo;

    private ShoppingListService service;

    @BeforeEach
    void setUp() {
        service = new ShoppingListService(riRepo, ingredientRepo, supplierRepo);
    }

    @Test
    void calculate_ShouldAggregateQuantitiesOfSameIngredient() {
        // GIVEN
        MenuItemDTO item1 = new MenuItemDTO(1L, 100L, "Receta A", "Lunes", "Almuerzo");
        MenuItemDTO item2 = new MenuItemDTO(2L, 101L, "Receta B", "Martes", "Almuerzo");
        
        // Mapa de selecciones: 10 comensales para cada receta
        Map<MenuItemDTO, Integer> selections = Map.of(item1, 10, item2, 10);

        // Ambas recetas usan el mismo ingrediente (ID 50)
        RecipeIngredient ri1 = new RecipeIngredient(100L, 50L, 0.1, MeasurementUnit.KILOGRAMO); // 100g por persona
        RecipeIngredient ri2 = new RecipeIngredient(101L, 50L, 0.2, MeasurementUnit.KILOGRAMO); // 200g por persona

        Ingredient ingredient = new Ingredient();
        ingredient.setId(50L);
        ingredient.setName("Papa");
        ingredient.setSupplierId(1L);

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Verdulería");

        when(riRepo.findAllByRecipeId(100L)).thenReturn(List.of(ri1));
        when(riRepo.findAllByRecipeId(101L)).thenReturn(List.of(ri2));
        when(ingredientRepo.findById(50L)).thenReturn(Optional.of(ingredient));
        when(supplierRepo.findById(1L)).thenReturn(Optional.of(supplier));

        // WHEN
        Map<String, List<ShoppingItemDTO>> result = service.calculate(selections);

        // THEN
        assertTrue(result.containsKey("Verdulería"));
        List<ShoppingItemDTO> compras = result.get("Verdulería");
        
        // (0.1 * 10) + (0.2 * 10) = 1.0 + 2.0 = 3.0 kg
        assertEquals(1, compras.size());
        assertEquals("Papa", compras.get(0).ingredientName());
        assertEquals(3.0, compras.get(0).totalAmount(), 0.001);
    }

    @Test
    void calculate_ShouldHandleZeroDiners() {
        // GIVEN: 0 comensales
        MenuItemDTO item = new MenuItemDTO(1L, 100L, "Receta", "Lunes", "Almuerzo");
        Map<MenuItemDTO, Integer> selections = Map.of(item, 0);

        // WHEN
        Map<String, List<ShoppingItemDTO>> result = service.calculate(selections);

        // THEN: El resultado debe estar vacío
        assertTrue(result.isEmpty());
        verifyNoInteractions(riRepo);
    }
}