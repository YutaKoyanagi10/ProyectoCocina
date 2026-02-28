package org.proyectococina.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococina.domain.dto.IngredientDTO;
import org.proyectococina.domain.model.Ingredient;
import org.proyectococina.domain.model.Supplier;
import org.proyectococina.repository.IngredientRepository;
import org.proyectococina.repository.SupplierRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock private IngredientRepository ingredientRepo;
    @Mock private SupplierRepository supplierRepo;

    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientService(ingredientRepo, supplierRepo);
    }

    @Test
    void findById_ShouldReturnCorrectDTO_WithFormattedDates() {
        // GIVEN
        Long ingId = 1L;
        Ingredient mockIng = new Ingredient();
        mockIng.setId(ingId);
        mockIng.setName("Pimienta");
        mockIng.setSupplierId(10L);
        mockIng.setInsertedAt(LocalDateTime.of(2026, 2, 28, 17, 45));

        Supplier mockSup = new Supplier();
        mockSup.setName("Especias del Sur");

        when(ingredientRepo.findById(ingId)).thenReturn(Optional.of(mockIng));
        when(supplierRepo.findById(10L)).thenReturn(Optional.of(mockSup));

        // WHEN
        Optional<IngredientDTO> result = ingredientService.findById(ingId);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Pimienta", result.get().getName());
        assertEquals("Especias del Sur", result.get().getSupplierName());
        assertEquals("28/02/2026 17:45", result.get().getInsertedAt());
    }

    @Test
    void save_ShouldThrowException_WhenSupplierDoesNotExist() {
        // GIVEN: Un DTO con un proveedor que no está en el repo
        IngredientDTO dto = new IngredientDTO(null, "Aceite", "Proveedor Inexistente", null, null);
        
        when(supplierRepo.findByName("Proveedor Inexistente")).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ingredientService.save(dto);
        });

        assertTrue(exception.getMessage().contains("El proveedor no existe"));
        verifyNoInteractions(ingredientRepo); // El ingrediente nunca debe guardarse si el proveedor falla
    }

    @Test
    void save_ShouldPersistNewIngredient_WhenSupplierExists() {
        // GIVEN
        IngredientDTO dto = new IngredientDTO(null, "Vinagre", "Distribuidora X", null, null);
        
        Supplier mockSup = new Supplier();
        mockSup.setId(5L);
        mockSup.setName("Distribuidora X");

        when(supplierRepo.findByName("Distribuidora X")).thenReturn(Optional.of(mockSup));

        // WHEN
        ingredientService.save(dto);

        // THEN
        verify(ingredientRepo).save(any(Ingredient.class));
        verify(ingredientRepo, never()).update(any());
    }

    @Test
    void save_ShouldUpdateIngredient_WhenIdIsProvided() {
        // GIVEN: Un DTO con ID (edición)
        IngredientDTO dto = new IngredientDTO(1L, "Sal Marina", "Salinas S.A.", null, null);
        
        Supplier mockSup = new Supplier();
        mockSup.setId(8L);
        when(supplierRepo.findByName("Salinas S.A.")).thenReturn(Optional.of(mockSup));

        // WHEN
        ingredientService.save(dto);

        // THEN
        verify(ingredientRepo).update(any(Ingredient.class));
        verify(ingredientRepo, never()).save(any());
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        // GIVEN
        IngredientDTO dto = new IngredientDTO(1L, "Borrar", "Prov", null, null);

        // WHEN
        ingredientService.delete(dto);

        // THEN
        verify(ingredientRepo).delete(any(Ingredient.class));
    }
}