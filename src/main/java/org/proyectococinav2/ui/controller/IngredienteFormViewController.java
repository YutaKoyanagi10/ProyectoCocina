package org.proyectococinav2.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.domain.model.Supplier;
import org.proyectococinav2.service.IngredientService;
import org.proyectococinav2.service.SupplierService;
import org.proyectococinav2.service.impl.SupplierServiceImpl;
import org.proyectococinav2.util.validators.IngredientValidator;
import org.proyectococinav2.repository.impl.SupplierRepositoryImpl;
import org.proyectococinav2.util.alert.ShowAlert;
import javafx.scene.control.Alert;
import org.proyectococinav2.service.impl.IngredientServiceImpl;
import org.proyectococinav2.repository.impl.IngredientRepositoryImpl;   
import java.util.List;

public class IngredienteFormViewController extends Controller {
    @FXML
    private TextField ingredientNameField;
    @FXML
    private ComboBox<String> proveedorComboBox;
    @FXML
    private Button onCancel;
    @FXML
    private Button onSaveIngredient;

    private final SupplierService supplierService = new SupplierServiceImpl(new SupplierRepositoryImpl());

    private final IngredientService ingredientService = new IngredientServiceImpl(new IngredientRepositoryImpl());

    @FXML
    public void initialize() {
        List<String> proveedores = supplierService.findAll().stream().map(Supplier::getName).toList();
        proveedorComboBox.setItems(FXCollections.observableArrayList(proveedores));
    }

    @FXML
    private void onCancel() {
        volverAlMenuPrincipal(ingredientNameField.getScene());
    }

    @FXML
    private void onSaveIngredient() {

        String nombre = ingredientNameField.getText();
        String proveedor = proveedorComboBox.getValue();
        if(!IngredientValidator.validateIngredientFields(nombre, proveedor)) {
            ShowAlert.show("Campos incompletos", Alert.AlertType.ERROR);
            return;
        }
        // Verificar si ya existe un ingrediente con el mismo nombre
        if (ingredientService.findIngredientByName(nombre).isPresent()) {
            ShowAlert.show("Ya existe un ingrediente con ese nombre.", Alert.AlertType.ERROR);
            return;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(nombre);
        
        var supplierIdOpt = supplierService.findSupplierIdByName(proveedor);
        if (supplierIdOpt.isPresent()) {
            ingredient.setSupplierId(supplierIdOpt.get());
        } else {
            ShowAlert.show("No se encontró el proveedor seleccionado.", Alert.AlertType.ERROR);
            return;
        }

        ingredientService.save(ingredient);
        ShowAlert.show("Ingrediente guardado exitosamente.", Alert.AlertType.INFORMATION);
        volverAlMenuPrincipal(ingredientNameField.getScene());
    }
}
