package org.proyectococinav2.service;

import org.proyectococinav2.domain.dto.SupplierDTO;
import org.proyectococinav2.domain.model.Supplier;
import org.proyectococinav2.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SupplierService implements IService<SupplierDTO, Long> {

    private final SupplierRepository repository = new SupplierRepository();

    @Override
    public List<SupplierDTO> findAll() {
        return repository.findAll().stream().map(s -> new SupplierDTO(
            s.getId(), s.getName(), s.getContactInfo(),
            s.getInsertedAt() != null ? s.getInsertedAt().toString() : "N/A",
            s.getUpdatedAt() != null ? s.getUpdatedAt().toString() : "N/A"
        )).collect(Collectors.toList());
    }

    @Override
    public void save(SupplierDTO dto) {
        Supplier s = new Supplier();
        s.setId(dto.getId());
        s.setName(dto.getName());
        s.setContactInfo(dto.getContactInfo());

        if (s.getId() == null) {
            repository.save(s);
        } else {
            repository.update(s);
        }
    }

    @Override
    public void delete(SupplierDTO dto) {
        Supplier s = new Supplier();
        s.setId(dto.getId());
        repository.delete(s);
    }

    @Override
    public Optional<SupplierDTO> findById(Long id) {
        return repository.findById(id).map(s -> new SupplierDTO(
            s.getId(), s.getName(), s.getContactInfo(),
            s.getInsertedAt().toString(), s.getUpdatedAt().toString()
        ));
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<String> findAllNames() {
        return repository.findAll().stream()
                .map(Supplier::getName)
                .collect(Collectors.toList());
    }
}