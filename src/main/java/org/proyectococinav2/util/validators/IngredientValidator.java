package org.proyectococinav2.util.validators;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IngredientValidator {

    public static boolean validateDuplicateIngredient(VBox ingredientBox, String newIngredient, ComboBox<?> currentCombo) {
        for (Node node : ingredientBox.getChildren()) {
            if (node instanceof HBox) {
                for (Node child : ((HBox) node).getChildren()) {
                    if (child instanceof ComboBox && child != currentCombo) {
                        ComboBox<?> combo = (ComboBox<?>) child;
                        Object value = combo.getValue();
                        if (value != null && value.equals(newIngredient)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean validateRecipeFields(String recipeName, String instructions) {
        return recipeName != null && !recipeName.trim().isEmpty() && instructions != null && !instructions.trim().isEmpty();
    }

    public static boolean validateIngredientFields(String ingredientName, String supplierName) {
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            return false;
        }
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
