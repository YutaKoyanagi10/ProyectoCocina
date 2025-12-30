package org.proyectococinav2.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

import org.proyectococinav2.domain.dto.RecipeDTO;
import org.proyectococinav2.domain.model.MeasurementUnit;
import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.repository.impl.IngredientRepositoryImpl;
import org.proyectococinav2.repository.impl.RecipeIngredientRepositoryImpl;
import org.proyectococinav2.service.impl.RecipeIngredientServiceImpl;
import org.proyectococinav2.service.IngredientService;
import org.proyectococinav2.service.impl.IngredientServiceImpl;
import org.proyectococinav2.util.validators.IngredientValidator;
import org.proyectococinav2.service.impl.RecipeServiceImpl;
import org.proyectococinav2.repository.impl.RecipeRepositoryImp;
import org.proyectococinav2.util.validators.RecipeFormValidator;
import org.proyectococinav2.util.alert.ShowAlert;



public class RecetaFormViewController extends Controller {

    @FXML
    private TextField recipeNameField;
    @FXML
    private TextArea instructionArea;
    @FXML
    private VBox ingredientBox;
    @FXML
    private ComboBox<String> comboIngredient;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitField;

    private final IngredientService ingredientService = new IngredientServiceImpl(new IngredientRepositoryImpl());

    private final RecipeServiceImpl recipeService = new RecipeServiceImpl(new RecipeRepositoryImp());

    private final RecipeIngredientServiceImpl recipeIngredientService = new RecipeIngredientServiceImpl(
        new RecipeIngredientRepositoryImpl()
    );

    @FXML
    private void onCancel() {
        volverAlMenuPrincipal(ingredientBox.getScene());
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void onAddIngredient() {
        HBox row = new HBox(10);

        ComboBox<String> comboIngredient = new ComboBox<>();
        comboIngredient.setPromptText("Ingrediente");
        comboIngredient.setPrefWidth(140);
        comboIngredient.setItems(FXCollections.observableArrayList(ingredientService.findAllNames()));
        comboIngredient.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (IngredientValidator.validateDuplicateIngredient(ingredientBox, newVal, comboIngredient)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "El ingrediente ya está añadido.", ButtonType.OK);
                alert.showAndWait();
                Platform.runLater(() -> {
                    if (!comboIngredient.getItems().isEmpty()) {
                        comboIngredient.setValue(null);
                        comboIngredient.setPromptText("Ingrediente");
                    }
                });
            }
        });
        Spinner<Double> quantity = new Spinner<>();
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 1, 0.1);
        quantity.setValueFactory(valueFactory);
        quantity.setPrefWidth(80);
        quantity.setEditable(true);
        
        ComboBox<MeasurementUnit> unit = new ComboBox<>();
        unit.setPromptText("Unidad");
        unit.setPrefWidth(100);
        unit.setItems(FXCollections.observableArrayList(MeasurementUnit.values()));
        
        Button btnDelete = new Button("Borrar");
        btnDelete.setOnAction(e -> ingredientBox.getChildren().remove(row));
        btnDelete.setPrefWidth(80);

        row.getChildren().addAll(comboIngredient,quantity, unit, btnDelete);
        ingredientBox.getChildren().add(row);
    }

    @FXML
    private void onSaveRecipe() {
        String nombre = recipeNameField.getText();
        String instrucciones = instructionArea.getText();
        // Validar el formulario
        if (!RecipeFormValidator.validate(nombre, instrucciones, ingredientBox)) {
            return;
        }
        var recetaExistente = recipeService.findRecipeByName(nombre);
        Recipe recipe = new Recipe();
        if(recetaExistente.isPresent()) {
            recipe = recetaExistente.get();
        }
        recipe.setName(nombre);
        recipe.setInstructions(instrucciones);
        recipeService.save(recipe);
    
        // Obtener el ID de la receta (recién guardada o actualizada)
        var recipeIdOpt = recipeService.findRecipeIdByName(nombre);
        if (recipeIdOpt.isEmpty()) {
            ShowAlert.show("No se pudo obtener el ID de la receta guardada.", Alert.AlertType.ERROR);
            return;
        }
        long recipeId = recipeIdOpt.get();

        // Guardar ingredientes asociados
        var ingredientRepo = new IngredientRepositoryImpl();
        for (var node : ingredientBox.getChildren()) {
            if (node instanceof HBox row) {
                ComboBox<?> combo = (ComboBox<?>) row.getChildren().get(0);
                Spinner<?> spinner = (Spinner<?>) row.getChildren().get(1);
                ComboBox<?> unitCombo = (ComboBox<?>) row.getChildren().get(2);

                String ingredientName = (String) combo.getValue();
                if (ingredientName == null) continue;
                var ingredientIdOpt = ingredientRepo.findIngredientIdByName(ingredientName);
                if (ingredientIdOpt.isEmpty()) continue;
                long ingredientId = ingredientIdOpt.get();
                double cantidad = ((Number) spinner.getValue()).doubleValue();
                var unit = (MeasurementUnit) unitCombo.getValue();
                if (unit == null) continue;

                var recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipeId(recipeId);
                recipeIngredient.setIngredientId(ingredientId);
                recipeIngredient.setServingPerPerson(cantidad);
                recipeIngredient.setUnit(unit);
                recipeIngredientService.save(recipeIngredient);
            }
        }

        ShowAlert.show("Receta guardada exitosamente.", Alert.AlertType.INFORMATION);
        
        volverAlMenuPrincipal(ingredientBox.getScene());
    }

    public void setRecipe(RecipeDTO recipe) {
        recipeNameField.setText(recipe.getName());
        instructionArea.setText(recipe.getInstructions());

        var ingredients = recipeIngredientService.findAllByRecipeId(recipe.getId());
        
        for (RecipeIngredient ri : ingredients) {
            HBox row = new HBox(10);

            ComboBox<String> comboIngredient = new ComboBox<>();
            comboIngredient.setPromptText("Ingrediente");
            comboIngredient.setPrefWidth(140);
            comboIngredient.setItems(FXCollections.observableArrayList(ingredientService.findAllNames()));
            var ingredientOpt = ingredientService.findById(ri.getIngredientId());
            ingredientOpt.ifPresent(ingredient -> comboIngredient.setValue(ingredient.getName()));

            Spinner<Double> quantity = new Spinner<>();
            SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, ri.getServingPerPerson(), 0.1);
            quantity.setValueFactory(valueFactory);
            quantity.setPrefWidth(80);
            quantity.setEditable(true);
            
            ComboBox<MeasurementUnit> unit = new ComboBox<>();
            unit.setPromptText("Unidad");
            unit.setPrefWidth(100);
            unit.setItems(FXCollections.observableArrayList(MeasurementUnit.values()));
            unit.setValue(ri.getUnit());
            
            Button btnDelete = new Button("Borrar");
            btnDelete.setOnAction(e -> ingredientBox.getChildren().remove(row));
            btnDelete.setPrefWidth(80);

            row.getChildren().addAll(comboIngredient, quantity, unit, btnDelete);
            ingredientBox.getChildren().add(row);
        }
    }
}