package org.proyectococinav2.service;

import org.proyectococinav2.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {
    void save(Supplier supplier);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Supplier> findById(Long id);
    List<Supplier> findAll();
    Optional<Supplier> findSupplierByName(String name);
    Optional<Long> findSupplierIdByName(String name);
}
