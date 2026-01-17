package org.proyectococinav2.ui.controller;

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
import org.proyectococinav2.domain.dto.SupplierDTO;
import org.proyectococinav2.service.SupplierService;

import java.io.IOException;

public class ProveedoresViewController {

    @FXML private TableView<SupplierDTO> proveedoresTable;
    @FXML private TableColumn<SupplierDTO, Long> idColumn;
    @FXML private TableColumn<SupplierDTO, String> nombreColumn;
    @FXML private TableColumn<SupplierDTO, String> contactoColumn;
    @FXML private TableColumn<SupplierDTO, String> fechaAltaColumn;
    @FXML private TableColumn<SupplierDTO, String> fechaModColumn;
    @FXML private TableColumn<SupplierDTO, Void> accionesColumn;
    @FXML private TextField searchField;
    private ObservableList<SupplierDTO> masterData = FXCollections.observableArrayList();
    private final SupplierService supplierService = new SupplierService();

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarAcciones();
        setupFiltrado();
        cargarDatos();
    }

    private void configurarColumnas() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        fechaAltaColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        fechaModColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltrado() {
        // 1. Crear el filtro envolviendo la masterData
        FilteredList<SupplierDTO> filteredData = new FilteredList<>(masterData, p -> true);
        // 2. Escuchar cambios en el buscador
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(supplier -> {
                if (newVal == null || newVal.isBlank()) return true;
                
                String filter = newVal.toLowerCase();
                return (supplier.getName() != null && supplier.getName().toLowerCase().contains(filter)) || 
                       (supplier.getContactInfo() != null && supplier.getContactInfo().toLowerCase().contains(filter));
            });
        });
        // 3. Setup de ordenamiento (SortedList)
        SortedList<SupplierDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(proveedoresTable.comparatorProperty());

        // 4. Conectar a la tabla
        proveedoresTable.setItems(sortedData);
    }

    private void cargarDatos() {
        masterData.setAll(supplierService.findAll()); 
    }

    private void configurarAcciones() {
        accionesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(10, btnVer, btnEliminar);

            {
                box.setAlignment(Pos.CENTER);
                btnVer.setOnAction(event -> verProveedor(getTableRow().getItem()));
                btnEliminar.setOnAction(event -> eliminarProveedor(getTableRow().getItem()));
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

    private void verProveedor(SupplierDTO supplier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococinav2/ui/view/ProveedorFormView.fxml"));
            Parent root = loader.load();

            ProveedorFormViewController controller = loader.getController();
            controller.setSupplier(supplier);

            StackPane contentArea = (StackPane) proveedoresTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProveedor(SupplierDTO supplier) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar al proveedor '" + supplier.getName() + "'? Se borrarán sus ingredientes asociados.", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                supplierService.delete(supplier);
                cargarDatos();
            }
        });
    }
}