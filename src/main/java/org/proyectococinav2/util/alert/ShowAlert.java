package org.proyectococinav2.util.alert;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlert {
    public static void show(String mensaje, Alert.AlertType type) {
        Alert alert = new Alert(type, mensaje, ButtonType.OK);
        alert.showAndWait();
    }
    
}
