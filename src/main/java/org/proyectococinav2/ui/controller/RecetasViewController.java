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
import org.proyectococinav2.domain.dto.RecipeDTO;
import org.proyectococinav2.service.RecipeService;

import java.io.IOException;

public class RecetasViewController {

    @FXML private TableView<RecipeDTO> recetasTable;
    @FXML private TableColumn<RecipeDTO, Long> idColumn;
    @FXML private TableColumn<RecipeDTO, String> nombreColumn;
    @FXML private TableColumn<RecipeDTO, String> instruccionColumn;
    @FXML private TableColumn<RecipeDTO, Void> accionesColumn;
    @FXML private TableColumn<RecipeDTO, String> fechaAltaColumn;
    @FXML private TableColumn<RecipeDTO, String> fechaModColumn;
    @FXML private TextField searchField;

    private final ObservableList<RecipeDTO> masterData = FXCollections.observableArrayList();

    private final RecipeService recipeService = new RecipeService(); 

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarAcciones();
        setupFiltrado();
        cargarRecetas();
    }

    private void configurarColumnas() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        instruccionColumn.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        fechaAltaColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        fechaModColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltrado() {
        FilteredList<RecipeDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(receta -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();
                
                return (receta.getName() != null && receta.getName().toLowerCase().contains(filter)) ||
                       (receta.getInstructions() != null && receta.getInstructions().toLowerCase().contains(filter));
            });
        });

        SortedList<RecipeDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(recetasTable.comparatorProperty());
        recetasTable.setItems(sortedData);
    }

    private void cargarRecetas() {
        masterData.setAll(recipeService.findAll());
    }

    private void configurarAcciones() {
        accionesColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox container = new HBox(10, btnVer, btnEliminar);

            {
                container.setAlignment(Pos.CENTER);
                btnVer.setOnAction(e -> verReceta(getTableRow().getItem()));
                btnEliminar.setOnAction(e -> eliminarReceta(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void verReceta(RecipeDTO receta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococinav2/ui/view/RecetaFormView.fxml"));
            Parent root = loader.load();

            RecetaFormViewController controller = loader.getController();
            controller.setRecipe(receta);

            StackPane contentArea = (StackPane) recetasTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el formulario de edición: " + e.getMessage());
        }
    }

    private void eliminarReceta(RecipeDTO receta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Seguro que deseas eliminar la receta '" + receta.getName() + "'?", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // El servicio maneja el borrado en cascada
                recipeService.delete(receta);
                cargarRecetas(); // Refrescamos la tabla
            }
        });
    }
}