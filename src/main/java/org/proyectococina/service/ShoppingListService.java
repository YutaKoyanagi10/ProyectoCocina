package org.proyectococina.service;

import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.domain.dto.ShoppingItemDTO;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.repository.RecipeIngredientRepository;
import org.proyectococina.repository.IngredientRepository;
import org.proyectococina.repository.SupplierRepository;

import java.util.*;

public class ShoppingListService {

    private final RecipeIngredientRepository riRepo = new RecipeIngredientRepository();
    private final IngredientRepository ingredientRepo = new IngredientRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();

    public Map<String, List<ShoppingItemDTO>> calculate(Map<MenuItemDTO, Integer> selections) {

        Map<String, Map<String, ShoppingItemDTO>> accumulator = new HashMap<>();

        selections.forEach((item, diners) -> {
            if (diners <= 0) return;

            riRepo.findAllByRecipeId(item.getRecipeId()).forEach(ri -> {
                var ingredient = ingredientRepo.findById(ri.getIngredientId()).orElseThrow();
                var supplier = supplierRepo.findById(ingredient.getSupplierId()).orElse(null);
                
                String supplierName = (supplier != null) ? supplier.getName() : "Sin Proveedor";
                String ingName = ingredient.getName();
                MeasurementUnit unit = ri.getUnit();
                double amount = ri.getServingPerPerson() * diners;

                accumulator.computeIfAbsent(supplierName, k -> new HashMap<>());
                
                Map<String, ShoppingItemDTO> supplierItems = accumulator.get(supplierName);
                
                if (supplierItems.containsKey(ingName)) {
                    ShoppingItemDTO existing = supplierItems.get(ingName);
                    supplierItems.put(ingName, new ShoppingItemDTO(
                        ingName, 
                        existing.totalAmount() + amount, 
                        unit.getAbreviatura()
                    ));
                } else {
                    supplierItems.put(ingName, new ShoppingItemDTO(ingName, amount, unit.getAbreviatura()));
                }
            });
        });

        Map<String, List<ShoppingItemDTO>> finalResult = new HashMap<>();
        accumulator.forEach((supplier, itemsMap) -> {
            finalResult.put(supplier, new ArrayList<>(itemsMap.values()));
        });

        return finalResult;
    }
}