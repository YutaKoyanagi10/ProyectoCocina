package org.proyectococinav2.repository;

import org.proyectococinav2.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends GenericRepository<Ingredient, Long> {
    List<Ingredient> findAllBySupplierId(Long supplierId);
    Optional<Ingredient> findIngredientByName(String name);
    Optional<Long> findIngredientIdByName(String name);
    Optional<Ingredient> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<String> findAllNames();
}
