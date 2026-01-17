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
import org.proyectococinav2.domain.dto.IngredientDTO;
import org.proyectococinav2.service.IngredientService;

import java.io.IOException;

public class IngredientesViewController {

    @FXML private TableView<IngredientDTO> ingredientesTable;
    @FXML private TableColumn<IngredientDTO, Long> idColumn;
    @FXML private TableColumn<IngredientDTO, String> nombreColumn;
    @FXML private TableColumn<IngredientDTO, String> proveedorColumn;
    @FXML private TableColumn<IngredientDTO, String> fechaAltaColumn; 
    @FXML private TableColumn<IngredientDTO, String> fechaModColumn; 
    @FXML private TableColumn<IngredientDTO, Void> accionesColumn;
    @FXML private TextField searchField;

    private final ObservableList<IngredientDTO> masterData = FXCollections.observableArrayList();
    private final IngredientService ingredientService = new IngredientService();

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
        proveedorColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        fechaAltaColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        fechaModColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltrado() {
        FilteredList<IngredientDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(ing -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();
                
                // Filtramos por nombre del ingrediente o nombre del proveedor
                return (ing.getName() != null && ing.getName().toLowerCase().contains(filter)) ||
                       (ing.getSupplierName() != null && ing.getSupplierName().toLowerCase().contains(filter));
            });
        });

        SortedList<IngredientDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ingredientesTable.comparatorProperty());
        ingredientesTable.setItems(sortedData);
    }

    private void cargarDatos() {
        masterData.setAll(ingredientService.findAll());
    }

    private void configurarAcciones() {
        accionesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(10, btnVer, btnEliminar);

            {
                box.setAlignment(Pos.CENTER);
                btnVer.setOnAction(event -> verIngrediente(getTableRow().getItem()));
                btnEliminar.setOnAction(event -> confirmarEliminacion(getTableRow().getItem()));
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

    private void confirmarEliminacion(IngredientDTO ing) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar el ingrediente '" + ing.getName() + "'? Esto podría afectar a las recetas que lo usan.", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ingredientService.delete(ing);
                cargarDatos();
            }
        });
    }

    private void verIngrediente(IngredientDTO ing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococinav2/ui/view/IngredienteFormView.fxml"));
            Parent root = loader.load();

            IngredienteFormViewController controller = loader.getController();
            controller.setIngredient(ing);

            StackPane contentArea = (StackPane) ingredientesTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}