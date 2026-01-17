package org.proyectococina.service;

import org.proyectococina.domain.dto.IngredientDTO;
import org.proyectococina.domain.model.Ingredient;
import org.proyectococina.repository.IngredientRepository;
import org.proyectococina.repository.SupplierRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IngredientService implements IService<IngredientDTO, Long> {

    private final IngredientRepository ingredientRepo = new IngredientRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public List<IngredientDTO> findAll() {
        return ingredientRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<IngredientDTO> findById(Long id) {
        return ingredientRepo.findById(id).map(this::mapToDTO);
    }

    @Override
    public void save(IngredientDTO dto) {
        Ingredient entity = new Ingredient();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        supplierRepo.findByName(dto.getSupplierName())
                .ifPresent(s -> entity.setSupplierId(s.getId()));

        if (entity.getId() == null) {
            ingredientRepo.save(entity);
        } else {
            ingredientRepo.update(entity);
        }
    }

    @Override
    public void delete(IngredientDTO dto) {
        Ingredient entity = new Ingredient();
        entity.setId(dto.getId());
        ingredientRepo.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return ingredientRepo.existsById(id);
    }

    @Override
    public List<String> findAllNames(){
        return ingredientRepo.findAll().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    private IngredientDTO mapToDTO(Ingredient ing) {
        String sName = supplierRepo.findById(ing.getSupplierId())
                .map(s -> s.getName()).orElse("N/A");

        return new IngredientDTO(
            ing.getId(),
            ing.getName(),
            sName,
            ing.getInsertedAt() != null ? ing.getInsertedAt().format(formatter) : "N/A",
            ing.getUpdatedAt() != null ? ing.getUpdatedAt().format(formatter) : "N/A"
        );
    }
}