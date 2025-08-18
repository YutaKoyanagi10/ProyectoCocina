package org.proyectococinav2.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.proyectococinav2.domain.model.MeasurementUnit;
import org.proyectococinav2.repository.impl.IngredientRepositoryImpl;
import org.proyectococinav2.service.IngredientService;
import org.proyectococinav2.service.impl.IngredientServiceImpl;
import org.proyectococinav2.util.validators.IngredientValidator;


public class InsertarRecetaViewController {

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

    private Scene menuScene;
    private final IngredientService ingredientService = new IngredientServiceImpl(new IngredientRepositoryImpl());

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) ingredientBox.getScene().getWindow();
        stage.setScene(menuScene);
        stage.setTitle("Menú Principal");
        stage.show();
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
        // Lógica para guardar la receta
    }
}