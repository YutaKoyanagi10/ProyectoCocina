module org.proyectococina {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    opens org.proyectococina to javafx.fxml;
    opens org.proyectococina.ui.controller to javafx.fxml;
    exports org.proyectococina;
    exports org.proyectococina.domain.dto;
    exports org.proyectococina.ui.controller to javafx.fxml;
}