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
    private Button insertRecipeButton;
    @FXML
    private Button insertIngredientButton;
    @FXML
    private Button insertSupplierButton;
    @FXML
    private Button recipesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button suppliersButton;
    @FXML
    private Button insertWeeklyMenuButton;
    @FXML
    private Button exportShoppingListButton;
    @FXML
    private Button weeklyMenuButton;

    @FXML void initialize() {
        Platform.runLater(() -> {
        onRecipes(); 
    });
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            contentArea.getChildren().setAll(root);

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Proyecto Cocina - " + title);

        } catch (IOException e) {
            System.err.println("Error crítico al cargar: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void onInsertRecipe() {
        loadView("/org/proyectococina/ui/view/RecipeFormView.fxml", "Insertar Receta");
    }

    @FXML
    private void onInsertIngredient() {
        loadView("/org/proyectococina/ui/view/IngredientFormView.fxml", "Insertar Ingrediente");
    }

    @FXML
    private void onInsertSupplier() {
        loadView("/org/proyectococina/ui/view/SupplierFormView.fxml", "Insertar Proveedor");
    }

    @FXML
    private void onRecipes() {
        loadView("/org/proyectococina/ui/view/RecipesView.fxml", "Recetas");
    }

    @FXML
    private void onIngredients() {
        loadView("/org/proyectococina/ui/view/IngredientsView.fxml", "Ingredientes");
    }

    @FXML
    private void onSuppliers() {
        loadView("/org/proyectococina/ui/view/SuppliersView.fxml", "Proveedores");
    }

    @FXML
    private void onInsertWeeklyMenu() {
        loadView("/org/proyectococina/ui/view/WeeklyMenuFormView.fxml", "Insertar Menú Semanal");
    }

    @FXML
    private void onExportShoppingList() {
        loadView("/org/proyectococina/ui/view/ShoppingListView.fxml", "Lista de Compras");
    }

    @FXML
    private void onWeeklyMenu() {
        loadView("/org/proyectococina/ui/view/WeeklyMenuView.fxml", "Menú Semanal");
    }
}