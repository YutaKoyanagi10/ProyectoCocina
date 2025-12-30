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
    private Button insertarMenuSemanalButton;
    @FXML
    private Button exportarListaDeComprasButton;
    @FXML
    private Button menuSemanalButton;

    @FXML void initialize() {

    }

    private void setSceneTitleAndShow(String fxmlpath, String title, Scene scene) {
        try {
            Stage stage = (Stage) scene.getWindow();
            Scene menuScene = stage.getScene();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Pasar la escena del menú al nuevo controlador si es necesario
            Controller controller = loader.getController();
            controller.setMenuScene(menuScene);

            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la vista: " + fxmlpath, e);
        }
    }

    @FXML
    private void onInsertarReceta() {
        setSceneTitleAndShow("/org/proyectococinav2/ui/view/RecetaFormView.fxml", "Insertar Receta", insertarRecetaButton.getScene());
    }

    @FXML
    private void onInsertarIngrediente() {
        setSceneTitleAndShow("/org/proyectococinav2/ui/view/IngredienteFormView.fxml", "Insertar Ingrediente", insertarIngredienteButton.getScene());
    }

    @FXML
    private void onInsertarProveedor() {
        // Lógica para insertar proveedor
    }

    @FXML
    private void onRecetas() {
        setSceneTitleAndShow("/org/proyectococinav2/ui/view/RecetasView.fxml", "Recetas", recetasButton.getScene());
    }

    @FXML
    private void onIngredientes() {
        
    }

    @FXML
    private void onProveedores() {
        // Lógica para mostrar proveedores
    }

    @FXML
    private void onInsertarMenuSemanal() {
        // Lógica para importar datos
    }

    @FXML
    private void onExportarListaDeCompras() {
        // Lógica para exportar datos
    }

    @FXML
    private void onMenuSemanal() {
        // Lógica para mostrar menú semanal
    }
}