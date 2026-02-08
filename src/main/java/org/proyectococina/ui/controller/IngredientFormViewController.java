package org.proyectococina.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;

import org.proyectococina.domain.dto.IngredientDTO;
import org.proyectococina.service.IngredientService;
import org.proyectococina.service.SupplierService;
import org.proyectococina.util.alert.ShowAlert;
import org.proyectococina.util.validators.IngredientFormValidator;

import java.io.IOException;
import java.util.List;

public class IngredientFormViewController {

    @FXML private TextField ingredientNameField;
    @FXML private ComboBox<String> supplierComboBox;
    @FXML private Label titleLabel;

    private final IngredientService ingredientService = new IngredientService();
    private final SupplierService supplierService = new SupplierService();

    private Long currentIngredientId = null;

    @FXML
    public void initialize() {
        List<String> suppliers = supplierService.findAllNames();
        supplierComboBox.setItems(FXCollections.observableArrayList(suppliers));
    }

    public void setIngredient(IngredientDTO ing) {
        if (ing == null) return;

        this.titleLabel.setText("Editar Ingrediente");
        this.currentIngredientId = ing.getId();

        Platform.runLater(() -> {
            if (ingredientNameField != null) {
                ingredientNameField.setText(ing.getName());
            }
            if (supplierComboBox != null) {
                supplierComboBox.setValue(ing.getSupplierName());
            }
        });
    }

    @FXML
    private void onSaveIngredient() {
        String name = ingredientNameField.getText();
        String supplierName = supplierComboBox.getValue();

        if (!IngredientFormValidator.validateIngredientFields(name, supplierName)) {
            ShowAlert.show("Por favor, completa todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        IngredientDTO dto = new IngredientDTO(currentIngredientId, name, supplierName);

        try {
            ingredientService.save(dto);
            ShowAlert.show("Ingrediente guardado exitosamente.", Alert.AlertType.INFORMATION);
            returnToList();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCancel() {
        returnToList();
    }

    private void returnToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/IngredientesView.fxml"));
            Parent root = loader.load();
            
            StackPane contentArea = (StackPane) ingredientNameField.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            ShowAlert.show("Error al cargar la vista de ingredientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}