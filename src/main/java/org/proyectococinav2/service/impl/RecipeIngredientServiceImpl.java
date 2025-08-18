package org.proyectococinav2.service.impl;

import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.repository.RecipeIngredientRepository;
import org.proyectococinav2.service.RecipeIngredientService;

import java.util.List;
import java.util.Optional;

public class RecipeIngredientServiceImpl implements RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeIngredientServiceImpl(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @Override
    public void save(RecipeIngredient recipeIngredient) {
        if(recipeIngredientRepository.existsById(recipeIngredient.getRecipeId(), recipeIngredient.getIngredientId())) {
            recipeIngredientRepository.update(recipeIngredient);
        } else {
            recipeIngredientRepository.save(recipeIngredient);
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        if (recipeIngredientRepository.existsById(recipeId, ingredientId)) {
            recipeIngredientRepository.deleteById(recipeId, ingredientId);
        } else {
            throw new IllegalArgumentException("RecipeIngredient with recipeId:" + recipeId + " and ingredientId:" + ingredientId + " does not exist.");
        }
    }

    @Override
    public boolean existsById(Long recipeId, Long ingredientId) {
        if (recipeId == null || ingredientId == null) {
            throw new IllegalArgumentException("Recipe ID and Ingredient ID cannot be null.");
        }
        return recipeIngredientRepository.existsById(recipeId, ingredientId);
    }

    @Override
    public Optional<RecipeIngredient> findById(Long recipeId, Long ingredientId) {
        return Optional.ofNullable(recipeIngredientRepository.findById(recipeId, ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("RecipeIngredient with recipeId " + recipeId + " and ingredientId " + ingredientId + " does not exist.")));
    }

    @Override
    public List<RecipeIngredient> findAllByRecipeId(Long recipeId) {
        if (recipeId == null) {
            throw new IllegalArgumentException("Recipe ID cannot be null.");
        }
        return recipeIngredientRepository.findAllByRecipeId(recipeId);
    }

    @Override
    public List<RecipeIngredient> findAllByIngredientId(Long ingredientId) {
        if (ingredientId == null) {
            throw new IllegalArgumentException("Ingredient ID cannot be null.");
        }
        return recipeIngredientRepository.findAllByIngredientId(ingredientId);
    }

    @Override
    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepository.findAll();
    }
}
