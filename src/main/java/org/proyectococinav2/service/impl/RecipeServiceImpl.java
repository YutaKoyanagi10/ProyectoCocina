package org.proyectococinav2.service.impl;

import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.repository.RecipeRepository;
import org.proyectococinav2.service.RecipeService;

import java.util.List;
import java.util.Optional;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void save(Recipe recipe) {
        if (recipeRepository.existsById(recipe.getId())) {
            recipeRepository.update(recipe);
        } else {
            recipeRepository.save(recipe);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Recipe with id " + id + " does not exist.");
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }else {
            return recipeRepository.existsById(id);
        }
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return Optional.ofNullable(recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Recipe with id " + id + " does not exist.")));
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> findRecipeByName(String name) {
        return Optional.ofNullable(recipeRepository.findRecipeByName(name).orElseThrow(() -> new IllegalArgumentException("Recipe with name " + name + " does not exist.")));
    }

    @Override
    public Optional<Long> findRecipeIdByName(String name) {
        return Optional.ofNullable(recipeRepository.findRecipeIdByName(name).orElseThrow(() -> new IllegalArgumentException("Recipe with name " + name + " does not exist.")));
    }
}
