package org.proyectococinav2.service;

import org.proyectococinav2.domain.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    void save(Recipe recipe);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Recipe> findById(Long id);
    List<Recipe> findAll();
    Optional<Recipe> findRecipeByName(String name);
    Optional<Long> findRecipeIdByName(String name);

}
