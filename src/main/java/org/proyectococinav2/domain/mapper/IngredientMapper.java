package org.proyectococinav2.domain.mapper;

import org.proyectococinav2.domain.dto.IngredientDTO;
import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.domain.model.Supplier;

public class IngredientMapper{

    public static IngredientDTO toDTO(Ingredient ingredient, Supplier supplier) {
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setSupplierId(ingredient.getSupplierId());
        dto.setSupplier(SupplierMapper.toDTO(supplier));
        return dto;
    }
    public static Ingredient toModel(IngredientDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setSupplierId(dto.getSupplierId());
        return ingredient;
    }
}
