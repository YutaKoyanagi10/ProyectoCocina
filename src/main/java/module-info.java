module org.proyectococinav2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    opens org.proyectococinav2 to javafx.fxml;
    opens org.proyectococinav2.ui.controller to javafx.fxml;
    exports org.proyectococinav2;
    exports org.proyectococinav2.ui.controller to javafx.fxml;
}