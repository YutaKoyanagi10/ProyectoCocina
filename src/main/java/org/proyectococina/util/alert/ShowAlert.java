package org.proyectococina.util.alert;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlert {
    public static void show(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
    
}
