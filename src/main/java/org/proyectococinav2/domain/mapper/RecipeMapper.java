package org.proyectococinav2.domain.mapper;

import org.proyectococinav2.domain.dto.RecipeDTO;
import org.proyectococinav2.domain.model.Recipe;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setInstructions(recipe.getInstructions());
        return dto;
    }

    public static Recipe toModel(RecipeDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setId(dto.getId());
        recipe.setName(dto.getName());
        recipe.setInstructions(dto.getInstructions());
        return recipe;
    }
}