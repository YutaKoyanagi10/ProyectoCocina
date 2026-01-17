package org.proyectococina.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.proyectococina.domain.dto.RecipeDTO;
import org.proyectococina.domain.dto.RecipeIngredientDTO;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.service.RecipeService;
import org.proyectococina.service.IngredientService;
import org.proyectococina.util.validators.RecipeFormValidator;
import org.proyectococina.util.alert.ShowAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecetaFormViewController {

    @FXML private TextField recipeNameField;
    @FXML private TextArea instructionArea;
    @FXML private VBox ingredientBox;

    private final RecipeService recipeService = new RecipeService();
    private final IngredientService ingredientService = new IngredientService();
    private Long currentRecipeId = null;

    @FXML
    private void onAddIngredient() {
        addRow(null);
    }

    @FXML
    private void onCancel() {
        volverAlListado();
    }

    @FXML
    private void onSaveRecipe() {
        String nombre = recipeNameField.getText();
        String instrucciones = instructionArea.getText();

        if (!RecipeFormValidator.validate(nombre, instrucciones, ingredientBox)) {
            return;
        }

        // 1. Recolectar ingredientes de la UI y convertirlos en DTOs
        List<RecipeIngredientDTO> ingredientsDTO = new ArrayList<>();
        for (var node : ingredientBox.getChildren()) {
            if (node instanceof HBox row) {
                ComboBox<String> combo = (ComboBox<String>) row.getChildren().get(0);
                Spinner<Double> spinner = (Spinner<Double>) row.getChildren().get(1);
                ComboBox<MeasurementUnit> unitCombo = (ComboBox<MeasurementUnit>) row.getChildren().get(2);

                spinner.increment(0);

                if (combo.getValue() != null && unitCombo.getValue() != null) {
                    ingredientsDTO.add(new RecipeIngredientDTO(
                        combo.getValue(),
                        spinner.getValue(),
                        unitCombo.getValue()
                    ));
                }
            }
        }

        RecipeDTO recipeDTO = new RecipeDTO(currentRecipeId, nombre, instrucciones, ingredientsDTO);

        try {
            recipeService.save(recipeDTO);
            ShowAlert.show("Receta guardada exitosamente.", Alert.AlertType.INFORMATION);
            volverAlListado();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setRecipe(RecipeDTO recipe) {
        this.currentRecipeId = recipe.getId();
        recipeNameField.setText(recipe.getName());
        instructionArea.setText(recipe.getInstructions());
        ingredientBox.getChildren().clear();
        for (RecipeIngredientDTO ri : recipe.getIngredients()) {
            addRow(ri);
        }
    }

    private void addRow(RecipeIngredientDTO data) {
    HBox row = new HBox(15);
    row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    row.setPadding(new javafx.geometry.Insets(5));

    ComboBox<String> combo = new ComboBox<>();
    combo.setPromptText("Seleccionar ingrediente");
    HBox.setHgrow(combo, javafx.scene.layout.Priority.ALWAYS);
    combo.setPrefWidth(200); 
    combo.setItems(FXCollections.observableArrayList(ingredientService.findAllNames()));
    HBox.setHgrow(combo, javafx.scene.layout.Priority.ALWAYS);
    combo.setMaxWidth(Double.MAX_VALUE);
    
    Spinner<Double> quantity = new Spinner<>(0.0, 10000.0, 1.0, 0.5);
    quantity.setEditable(true);
    quantity.setPrefWidth(100);

    ComboBox<MeasurementUnit> unit = new ComboBox<>();
    unit.setPromptText("Unidad");
    unit.setPrefWidth(120);
    unit.setItems(FXCollections.observableArrayList(MeasurementUnit.values()));

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

    private void volverAlListado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/RecetasView.fxml"));
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