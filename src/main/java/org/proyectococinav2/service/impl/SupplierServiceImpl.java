package org.proyectococinav2.service.impl;

import org.proyectococinav2.domain.model.Supplier;
import org.proyectococinav2.repository.SupplierRepository;
import org.proyectococinav2.service.SupplierService;

import java.util.List;
import java.util.Optional;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }


    @Override
    public void save(Supplier supplier) {
        if (supplierRepository.existsById(supplier.getId())) {
            supplierRepository.update(supplier);
        } else {
            supplierRepository.save(supplier);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Supplier with id " + id + " does not exist.");
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        } else {
            return supplierRepository.existsById(id);
        }
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        return Optional.ofNullable(supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with id " + id + " does not exist.")));
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findSupplierByName(String name) {
        return Optional.ofNullable(supplierRepository.findSupplierByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with name " + name + " does not exist.")));
    }

    @Override
    public Optional<Long> findSupplierIdByName(String name) {
        return Optional.ofNullable(supplierRepository.findSupplierIdByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with name " + name + " does not exist.")));
    }
}
