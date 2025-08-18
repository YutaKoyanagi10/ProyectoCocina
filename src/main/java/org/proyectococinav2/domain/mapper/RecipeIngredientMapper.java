package org.proyectococinav2.domain.mapper;

import org.proyectococinav2.domain.dto.RecipeIngredientDTO;
import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.domain.model.Supplier;

public class RecipeIngredientMapper {
    public static RecipeIngredientDTO toDTO(RecipeIngredient model, Ingredient ingredient, Supplier supplier){
        RecipeIngredientDTO dto = new RecipeIngredientDTO();
        dto.setRecipeId(model.getRecipeId());
        dto.setIngredientId(model.getIngredientId());
        dto.setServingPerPerson(model.getServingPerPerson());
        dto.setUnit(model.getUnit());
        dto.setIngredient(IngredientMapper.toDTO(ingredient, supplier));
        return dto;
    }
    public static RecipeIngredient toModel(RecipeIngredientDTO dto) {
        RecipeIngredient model = new RecipeIngredient();
        model.setRecipeId(dto.getRecipeId());
        model.setIngredientId(dto.getIngredientId());
        model.setServingPerPerson(dto.getServingPerPerson());
        model.setUnit(dto.getUnit());
        return model;
    }
}
