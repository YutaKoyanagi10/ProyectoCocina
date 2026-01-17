package org.proyectococina.util.validators;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.proyectococina.util.alert.ShowAlert;

public class RecipeFormValidator {
    public static boolean validate(String nombre, String instrucciones, VBox ingredientBox) {
        if (nombre == null || nombre.isBlank()) {
            ShowAlert.show("El nombre de la receta es obligatorio.", Alert.AlertType.ERROR);
            return false;
        }
        if (instrucciones == null || instrucciones.isBlank()) {
            ShowAlert.show("Las instrucciones son obligatorias.", Alert.AlertType.ERROR);
            return false;
        }
        if (ingredientBox.getChildren().isEmpty()) {
            ShowAlert.show("Debe agregar al menos un ingrediente.", Alert.AlertType.ERROR);
            return false;
        }
        for (var node : ingredientBox.getChildren()) {
            if (node instanceof HBox row) {
                ComboBox<?> unitCombo = (ComboBox<?>) row.getChildren().get(2);
                if (unitCombo.getValue() == null) {
                    ShowAlert.show("Todos los ingredientes deben tener una unidad seleccionada.", Alert.AlertType.ERROR);
                    return false;
                }
            }
        }
        return true;
    }
}
