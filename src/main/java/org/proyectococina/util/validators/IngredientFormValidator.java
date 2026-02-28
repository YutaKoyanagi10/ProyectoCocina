package org.proyectococina.util.validators;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IngredientFormValidator {

    public static boolean isDuplicate(VBox ingredientBox, String ingredientName, ComboBox<String> currentCombo) {
        if (ingredientName == null) return false;

        for (Node node : ingredientBox.getChildren()) {
            if (node instanceof HBox row) {
                if (!row.getChildren().isEmpty() && row.getChildren().get(0) instanceof ComboBox<?> combo) {
                    if (combo != currentCombo && ingredientName.equals(combo.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean validateIngredientFields(String ingredientName, String instructions) {
        return ingredientName != null && !ingredientName.trim().isEmpty() 
            && instructions != null && !instructions.trim().isEmpty();
    }
}