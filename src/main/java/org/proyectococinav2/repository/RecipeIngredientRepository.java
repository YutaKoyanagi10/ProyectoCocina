package org.proyectococinav2.repository;

import org.proyectococinav2.domain.model.RecipeIngredient;

import java.util.List;
import java.util.Optional;

public interface RecipeIngredientRepository extends GenericRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findAllByRecipeId(Long recipeId);
    List<RecipeIngredient> findAllByIngredientId(Long ingredientId);
    void deleteById(Long recipeId, Long ingredientId);
    boolean existsById(Long recipeId, Long ingredientId);
    Optional<RecipeIngredient> findById(Long recipeId, Long ingredientId);
}
