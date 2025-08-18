package org.proyectococinav2.repository;

import org.proyectococinav2.domain.model.Supplier;

import java.util.Optional;

public interface SupplierRepository extends GenericRepository<Supplier, Long> {
    Optional<Supplier> findSupplierByName(String name);
    Optional<Long> findSupplierIdByName(String name);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Supplier> findById(Long id);

}
