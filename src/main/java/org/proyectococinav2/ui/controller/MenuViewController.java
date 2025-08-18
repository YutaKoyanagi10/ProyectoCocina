package org.proyectococinav2.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuViewController {

    @FXML
    private Button insertarRecetaButton;
    @FXML
    private Button insertarIngredienteButton;
    @FXML
    private Button insertarProveedorButton;
    @FXML
    private Button recetasButton;
    @FXML
    private Button ingredientesButton;
    @FXML
    private Button proveedoresButton;
    @FXML
    private Button importarDatosButton;
    @FXML
    private Button exportarButton;
    @FXML
    private Button menuSemanalButton;

    @FXML void initialize() {

    }

    @FXML
    private void onInsertarReceta() {
        try {
            Stage stage = (Stage) insertarRecetaButton.getScene().getWindow();
            Scene menuScene = stage.getScene(); // Guarda la escena del menú

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococinav2/ui/View/InsertarRecetaView.fxml"));
            Parent insertarRecetaRoot = loader.load();
            Scene insertarRecetaScene = new Scene(insertarRecetaRoot);

            // Pasa la escena del menú al nuevo controlador
            InsertarRecetaViewController controller = loader.getController();
            controller.setMenuScene(menuScene);

            stage.setTitle("Insertar Receta");
            stage.setScene(insertarRecetaScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la vista de insertar receta", e);
        }
    }

    @FXML
    private void onInsertarIngrediente() {
        // Lógica para insertar ingrediente
    }

    @FXML
    private void onInsertarProveedor() {
        // Lógica para insertar proveedor
    }

    @FXML
    private void onRecetas() {
        // Lógica para mostrar recetas
    }

    @FXML
    private void onIngredientes() {
        // Lógica para mostrar ingredientes
    }

    @FXML
    private void onProveedores() {
        // Lógica para mostrar proveedores
    }

    @FXML
    private void onImportarDatos() {
        // Lógica para importar datos
    }

    @FXML
    private void onExportar() {
        // Lógica para exportar datos
    }

    @FXML
    private void onMenuSemanal() {
        // Lógica para mostrar menú semanal
    }
}