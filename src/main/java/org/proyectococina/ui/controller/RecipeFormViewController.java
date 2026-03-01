package org.proyectococina.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import org.proyectococina.domain.dto.RecipeDTO;
import org.proyectococina.domain.dto.RecipeIngredientDTO;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.service.RecipeService;
import org.proyectococina.service.IngredientService;
import org.proyectococina.util.validators.IngredientFormValidator;
import org.proyectococina.util.alert.ShowAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeFormViewController {

    @FXML private TextField recipeNameField;
    @FXML private TextArea instructionArea;
    @FXML private VBox ingredientBox;
    @FXML private Label titleLabel;

    private final RecipeService recipeService = new RecipeService();
    private final IngredientService ingredientService = new IngredientService();
    private Long currentRecipeId = null;

    @FXML
    private void onAddIngredient() {
        addRow(null);
    }

    @FXML
    private void onCancel() {
        returnToList();
    }

    @FXML
    private void onSaveRecipe() {
        if (!IngredientFormValidator.validateIngredientFields(recipeNameField.getText(), instructionArea.getText())) {
            ShowAlert.show("Nombre e instrucciones son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        List<RecipeIngredientDTO> ingredientsDTO = new ArrayList<>();
        for (var node : ingredientBox.getChildren()) {
            if (node instanceof HBox row) {
                ComboBox<String> combo = (ComboBox<String>) row.getChildren().get(0);
                Spinner<Double> spinner = (Spinner<Double>) row.getChildren().get(1);
                ComboBox<MeasurementUnit> unitCombo = (ComboBox<MeasurementUnit>) row.getChildren().get(2);

                spinner.increment(0);

                String ingredient = combo.getValue();
                if (ingredient != null && unitCombo.getValue() != null) {
                    if (IngredientFormValidator.isDuplicate(ingredientBox, ingredient, combo)) {
                        ShowAlert.show("Hay ingredientes duplicados: " + ingredient, Alert.AlertType.WARNING);
                        return;
                    }
                    
                    ingredientsDTO.add(new RecipeIngredientDTO(ingredient, spinner.getValue(), unitCombo.getValue()));
                } else {
                    ShowAlert.show("Todos los ingredientes deben tener un nombre y unidad válidos.", Alert.AlertType.WARNING);
                    return;
                }
            }
        }

        if (ingredientsDTO.isEmpty()) {
            ShowAlert.show("La receta debe tener al menos un ingrediente válido.", Alert.AlertType.WARNING);
            return;
        }

        try {
            recipeService.save(new RecipeDTO(currentRecipeId, recipeNameField.getText(), instructionArea.getText(), ingredientsDTO));
            ShowAlert.show("Receta guardada exitosamente.", Alert.AlertType.INFORMATION);
            returnToList();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addRow(RecipeIngredientDTO data) {
        HBox row = new HBox(15);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setPadding(new javafx.geometry.Insets(5));
        row.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> combo = new ComboBox<>();
        combo.setItems(FXCollections.observableArrayList(ingredientService.findAllNames()));
        combo.setPromptText("Seleccionar ingrediente");
        combo.setPrefWidth(200);

        HBox.setHgrow(combo, Priority.ALWAYS); 
        combo.setMaxWidth(Double.MAX_VALUE);    
        combo.setPrefWidth(200);
        combo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && IngredientFormValidator.isDuplicate(ingredientBox, newVal, combo)) {
                ShowAlert.show("El ingrediente '" + newVal + "' ya está en la lista.", Alert.AlertType.WARNING);
                Platform.runLater(() -> combo.setValue(null));
            }
        });

        Spinner<Double> quantity = new Spinner<>(0.0, 10000.0, 1.0, 0.5);
        quantity.setEditable(true);
        quantity.setPrefWidth(100);

        ComboBox<MeasurementUnit> unit = new ComboBox<>();
        unit.setPromptText("Unidad");
        unit.setItems(FXCollections.observableArrayList(MeasurementUnit.values()));
        unit.setPrefWidth(120);

        if (data != null) {
            combo.setValue(data.getIngredientName());
            quantity.getValueFactory().setValue(data.getQuantity());
            unit.setValue(data.getUnit());
        }

        Button btnDelete = new Button("Borrar");
        btnDelete.setOnAction(e -> ingredientBox.getChildren().remove(row));

        row.getChildren().addAll(combo, quantity, unit, btnDelete);
        ingredientBox.getChildren().add(row);
    }

    public void setRecipe(RecipeDTO recipe) {
        if (recipe == null) return;
        this.titleLabel.setText("Editar Receta");
        this.currentRecipeId = recipe.getId();
        recipeNameField.setText(recipe.getName());
        instructionArea.setText(recipe.getInstructions());
        ingredientBox.getChildren().clear();
        for (RecipeIngredientDTO ri : recipe.getIngredients()) {
            addRow(ri);
        }
    }

    private void returnToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/RecipesView.fxml"));
            Parent root = loader.load();
            StackPane contentArea = (StackPane) recipeNameField.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            ShowAlert.show("Error al volver al listado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}