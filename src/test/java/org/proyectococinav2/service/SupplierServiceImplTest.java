package org.proyectococinav2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococinav2.domain.model.Supplier;
import org.proyectococinav2.repository.SupplierRepository;
import org.proyectococinav2.service.impl.SupplierServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void save_updatesSupplierIfExists() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.existsById(1L)).thenReturn(true);

        supplierService.save(supplier);

        verify(supplierRepository).update(supplier);
        verify(supplierRepository, never()).save(supplier);
    }

    @Test
    void save_savesSupplierIfNotExists() {
        Supplier supplier = new Supplier();
        supplier.setId(2L);
        when(supplierRepository.existsById(2L)).thenReturn(false);

        supplierService.save(supplier);

        verify(supplierRepository).save(supplier);
        verify(supplierRepository, never()).update(supplier);
    }

    @Test
    void deleteById_deletesIfExists() {
        Long id = 1L;
        when(supplierRepository.existsById(id)).thenReturn(true);

        supplierService.deleteById(id);

        verify(supplierRepository).deleteById(id);
    }

    @Test
    void deleteById_throwsExceptionIfNotExists() {
        Long id = 99L;
        when(supplierRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> supplierService.deleteById(id));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void existsById_returnsTrueIfExists() {
        when(supplierRepository.existsById(1L)).thenReturn(true);

        assertTrue(supplierService.existsById(1L));
    }

    @Test
    void existsById_returnsFalseIfNotExists() {
        when(supplierRepository.existsById(2L)).thenReturn(false);

        assertFalse(supplierService.existsById(2L));
    }

    @Test
    void existsById_throwsExceptionIfIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> supplierService.existsById(null));
        assertEquals("ID cannot be null.", ex.getMessage());
    }

    @Test
    void findById_returnsSupplierIfExists() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Optional<Supplier> result = supplierService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findById_throwsExceptionIfNotExists() {
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> supplierService.findById(99L));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void findAll_returnsListOfSuppliers() {
        Supplier s1 = new Supplier();
        s1.setId(1L);
        Supplier s2 = new Supplier();
        s2.setId(2L);
        List<Supplier> suppliers = List.of(s1, s2);
        when(supplierRepository.findAll()).thenReturn(suppliers);

        List<Supplier> result = supplierService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(s1));
        assertTrue(result.contains(s2));
    }

    @Test
    void findSupplierByName_returnsSupplierIfExists() {
        String name = "Proveedor";
        Supplier supplier = new Supplier();
        supplier.setName(name);
        when(supplierRepository.findSupplierByName(name)).thenReturn(Optional.of(supplier));

        Optional<Supplier> result = supplierService.findSupplierByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    void findSupplierByName_throwsExceptionIfNotExists() {
        String name = "Inexistente";
        when(supplierRepository.findSupplierByName(name)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> supplierService.findSupplierByName(name));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void findSupplierIdByName_returnsIdIfExists() {
        String name = "Proveedor";
        Long id = 1L;
        when(supplierRepository.findSupplierIdByName(name)).thenReturn(Optional.of(id));

        Optional<Long> result = supplierService.findSupplierIdByName(name);

        assertTrue(result.isPresent());
        assertEquals(id, result.get());
    }

    @Test
    void findSupplierIdByName_throwsExceptionIfNotExists() {
        String name = "Inexistente";
        when(supplierRepository.findSupplierIdByName(name)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> supplierService.findSupplierIdByName(name));
        assertTrue(ex.getMessage().contains("does not exist"));
    }
}