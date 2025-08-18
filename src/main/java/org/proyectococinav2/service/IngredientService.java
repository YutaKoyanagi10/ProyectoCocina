package org.proyectococinav2.service;

import org.proyectococinav2.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    List<Ingredient> findAll();
    List<String> findAllNames();
    Optional<Ingredient> findById(Long id);
    void save(Ingredient ingredient);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Ingredient> findAllBySupplierId(Long supplierId);
    Optional<Ingredient> findIngredientByName(String name);
    Optional<Long> findIngredientIdByName(String name);
}
