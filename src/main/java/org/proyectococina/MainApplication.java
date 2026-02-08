package org.proyectococina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.proyectococina.config.DataBaseConfig;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/proyectococina/ui/view/MenuView.fxml")));
        
        primaryStage.setTitle("Sistema de Gestión de Cocina");
        
        Scene scene = new Scene(root, 1000, 700);
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setResizable(false); 
        primaryStage.show();
    }

    public static void main(String[] args) {

        DataBaseConfig.getInstance().createTables();
        launch(args);
    }
}