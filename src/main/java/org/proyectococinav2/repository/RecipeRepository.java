package org.proyectococinav2.repository;

import org.proyectococinav2.domain.model.Recipe;

import java.util.Optional;

public interface RecipeRepository extends GenericRepository<Recipe, Long> {
    Optional<Recipe> findRecipeByName(String name);
    Optional<Long> findRecipeIdByName(String name);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Recipe> findById(Long id);
}