package org.proyectococina.service;

import org.proyectococina.domain.dto.SupplierDTO;
import org.proyectococina.domain.model.Supplier;
import org.proyectococina.repository.SupplierRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SupplierService implements IService<SupplierDTO, Long> {

    private final SupplierRepository repository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SupplierService() {
        this.repository = new SupplierRepository();
    }

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SupplierDTO> findAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
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
        return repository.findById(id).map(this::mapToDTO);
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

    private SupplierDTO mapToDTO(Supplier s) {
        return new SupplierDTO(
            s.getId(), 
            s.getName(), 
            s.getContactInfo(),
            s.getInsertedAt() != null ? s.getInsertedAt().format(formatter) : "N/A",
            s.getUpdatedAt() != null ? s.getUpdatedAt().format(formatter) : "N/A"
        );
    }
}