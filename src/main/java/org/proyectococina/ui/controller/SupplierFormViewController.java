package org.proyectococina.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import org.proyectococina.domain.dto.SupplierDTO;
import org.proyectococina.service.SupplierService;
import org.proyectococina.util.alert.ShowAlert;
import org.proyectococina.util.validators.SupplierFormValidator;

public class SupplierFormViewController {

    @FXML private TextField supplierNameField;
    @FXML private TextArea contactInfoArea;
    @FXML private Label titleLabel;

    private final SupplierService supplierService = new SupplierService();
    private Long currentSupplierId = null;

    public void setSupplier(SupplierDTO supplier) {
        if (supplier == null) return;
        this.titleLabel.setText("Editar Proveedor");
        this.currentSupplierId = supplier.getId();

        Platform.runLater(() -> {
            if (supplierNameField != null) {
                supplierNameField.setText(supplier.getName());
            }
            if (contactInfoArea != null) {
                contactInfoArea.setText(supplier.getContactInfo());
            }
        });
    }

    @FXML
    private void onSaveSupplier() {
        String name = supplierNameField.getText();
        String contact = contactInfoArea.getText();

        if (!SupplierFormValidator.validate(name, contact)) {
            ShowAlert.show("Por favor, completa todos los campos del proveedor.", Alert.AlertType.ERROR);
            return;
        }

        SupplierDTO dto = new SupplierDTO(currentSupplierId, name, contact);

        try {
            supplierService.save(dto);
            ShowAlert.show("Proveedor guardado exitosamente.", Alert.AlertType.INFORMATION);
            returnToList();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar el proveedor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCancel() {
        returnToList();
    }

    private void returnToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/SuppliersView.fxml"));
            Parent root = loader.load();
            
            StackPane contentArea = (StackPane) supplierNameField.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            ShowAlert.show("Error al cargar la vista de proveedores: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}