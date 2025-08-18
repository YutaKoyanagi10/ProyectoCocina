package org.proyectococinav2.service;

import org.proyectococinav2.domain.model.RecipeIngredient;

import java.util.List;
import java.util.Optional;

public interface RecipeIngredientService {

    void save(RecipeIngredient recipeIngredient);
    void deleteById(Long recipeId, Long ingredientId);
    boolean existsById(Long recipeId, Long ingredientId);
    Optional<RecipeIngredient> findById(Long recipeId, Long ingredientId);
    List<RecipeIngredient> findAllByRecipeId(Long recipeId);
    List<RecipeIngredient> findAllByIngredientId(Long ingredientId);
    List<RecipeIngredient> findAll();
}
