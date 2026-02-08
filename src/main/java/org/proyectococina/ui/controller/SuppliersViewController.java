package org.proyectococina.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.proyectococina.domain.dto.SupplierDTO;
import org.proyectococina.service.SupplierService;

import java.io.IOException;

public class SuppliersViewController {

    @FXML private TableView<SupplierDTO> suppliersTable;
    @FXML private TableColumn<SupplierDTO, Long> idColumn;
    @FXML private TableColumn<SupplierDTO, String> nameColumn;
    @FXML private TableColumn<SupplierDTO, String> contactInfoColumn;
    @FXML private TableColumn<SupplierDTO, String> insertedAtColumn;
    @FXML private TableColumn<SupplierDTO, String> updatedAtColumn;
    @FXML private TableColumn<SupplierDTO, Void> actionsColumn;
    @FXML private TextField searchField;
    private ObservableList<SupplierDTO> masterData = FXCollections.observableArrayList();
    private final SupplierService supplierService = new SupplierService();

    @FXML
    public void initialize() {
        configureColumns();
        configureActions();
        setupFiltering();
        loadData();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        insertedAtColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltering() {
        FilteredList<SupplierDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(supplier -> {
                if (newVal == null || newVal.isBlank()) return true;
                
                String filter = newVal.toLowerCase();
                return (supplier.getName() != null && supplier.getName().toLowerCase().contains(filter)) || 
                       (supplier.getContactInfo() != null && supplier.getContactInfo().toLowerCase().contains(filter));
            });
        });
 
        SortedList<SupplierDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(suppliersTable.comparatorProperty());

        suppliersTable.setItems(sortedData);
    }

    private void loadData() {
        masterData.setAll(supplierService.findAll()); 
    }

    private void configureActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("Ver");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox box = new HBox(10, viewButton, deleteButton);

            {
                box.setAlignment(Pos.CENTER);
                viewButton.setOnAction(event -> viewSupplier(getTableRow().getItem()));
                deleteButton.setOnAction(event -> deleteSupplier(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
    }

    private void viewSupplier(SupplierDTO supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/SupplierFormView.fxml"));
            Parent root = loader.load();

            SupplierFormViewController controller = loader.getController();
            controller.setSupplier(supplier);

            StackPane contentArea = (StackPane) suppliersTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSupplier(SupplierDTO supplier) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar al proveedor '" + supplier.getName() + "'? Se borrarán sus ingredientes asociados.", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                supplierService.delete(supplier);
                loadData();
            }
        });
    }
}