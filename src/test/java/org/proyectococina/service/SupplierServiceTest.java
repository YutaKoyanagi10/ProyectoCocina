package org.proyectococina.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.proyectococina.domain.dto.SupplierDTO;
import org.proyectococina.domain.model.Supplier;
import org.proyectococina.repository.SupplierRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock private SupplierRepository repository;
    private SupplierService service;

    @BeforeEach
    void setUp() {
        service = new SupplierService(repository);
    }

    @Test
    void findById_ShouldFormatDatesCorrectly() {
        // GIVEN
        Supplier mockSupplier = new Supplier();
        mockSupplier.setId(1L);
        mockSupplier.setName("Distribuidora La Plata");
        mockSupplier.setInsertedAt(java.time.LocalDateTime.of(2026, 2, 28, 10, 0));

        when(repository.findById(1L)).thenReturn(Optional.of(mockSupplier));

        // WHEN
        Optional<SupplierDTO> result = service.findById(1L);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("28/02/2026 10:00", result.get().getInsertedAt());
        assertEquals("N/A", result.get().getUpdatedAt());
    }

    @Test
    void save_ShouldCallSave_WhenIdIsNull() {
        // GIVEN
        SupplierDTO dto = new SupplierDTO(null, "Nuevo Proveedor", "Calle 123", null, null);

        // WHEN
        service.save(dto);

        // THEN
        verify(repository).save(any(Supplier.class));
        verify(repository, never()).update(any());
    }

    @Test
    void save_ShouldCallUpdate_WhenIdIsNotNull() {
        // GIVEN
        SupplierDTO dto = new SupplierDTO(5L, "Proveedor Existente", "Calle 456", null, null);

        // WHEN
        service.save(dto);

        // THEN
        verify(repository).update(any(Supplier.class));
        verify(repository, never()).save(any());
    }
}