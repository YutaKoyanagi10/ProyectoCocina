package org.proyectococina.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class MenuViewController {

    @FXML
    private StackPane contentArea;
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
        Platform.runLater(() -> {
        onRecetas(); 
    });
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // 1. Limpiamos el centro y ponemos la nueva vista
            contentArea.getChildren().setAll(root);

            // 2. Actualizamos el título de la ventana principal (opcional)
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Proyecto Cocina - " + title);

        } catch (IOException e) {
            System.err.println("Error crítico al cargar: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void onInsertarReceta() {
        loadView("/org/proyectococina/ui/view/RecetaFormView.fxml", "Insertar Receta");
    }

    @FXML
    private void onInsertarIngrediente() {
        loadView("/org/proyectococina/ui/view/IngredienteFormView.fxml", "Insertar Ingrediente");
    }

    @FXML
    private void onInsertarProveedor() {
        loadView("/org/proyectococina/ui/view/ProveedorFormView.fxml", "Insertar Proveedor");
    }

    @FXML
    private void onRecetas() {
        loadView("/org/proyectococina/ui/view/RecetasView.fxml", "Recetas");
    }

    @FXML
    private void onIngredientes() {
        loadView("/org/proyectococina/ui/view/IngredientesView.fxml", "Ingredientes");
    }

    @FXML
    private void onProveedores() {
        loadView("/org/proyectococina/ui/view/ProveedoresView.fxml", "Proveedores");
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