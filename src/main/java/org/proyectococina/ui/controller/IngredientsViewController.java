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
import org.proyectococina.domain.dto.IngredientDTO;
import org.proyectococina.service.IngredientService;

import java.io.IOException;

public class IngredientsViewController {

    @FXML private TableView<IngredientDTO> ingredientsTable;
    @FXML private TableColumn<IngredientDTO, Long> idColumn;
    @FXML private TableColumn<IngredientDTO, String> nameColumn;
    @FXML private TableColumn<IngredientDTO, String> supplierColumn;
    @FXML private TableColumn<IngredientDTO, String> insertedAtColumn; 
    @FXML private TableColumn<IngredientDTO, String> updatedAtColumn; 
    @FXML private TableColumn<IngredientDTO, Void> actionsColumn;
    @FXML private TextField searchField;

    private final ObservableList<IngredientDTO> masterData = FXCollections.observableArrayList();
    private final IngredientService ingredientService = new IngredientService();

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
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        insertedAtColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltering() {
        FilteredList<IngredientDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(ing -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();
                
                return (ing.getName() != null && ing.getName().toLowerCase().contains(filter)) ||
                       (ing.getSupplierName() != null && ing.getSupplierName().toLowerCase().contains(filter));
            });
        });

        SortedList<IngredientDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ingredientsTable.comparatorProperty());
        ingredientsTable.setItems(sortedData);
    }

    private void loadData() {
        masterData.setAll(ingredientService.findAll());
    }

    private void configureActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("Ver");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox box = new HBox(10, viewButton, deleteButton);

            {
                box.setAlignment(Pos.CENTER);
                viewButton.setOnAction(event -> viewIngredient(getTableRow().getItem()));
                deleteButton.setOnAction(event -> confirmDeletion(getTableRow().getItem()));
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

    private void confirmDeletion(IngredientDTO ing) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar el ingrediente '" + ing.getName() + "'? Esto podría afectar a las recetas que lo usan.", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ingredientService.delete(ing);
                loadData();
            }
        });
    }

    private void viewIngredient(IngredientDTO ing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/IngredientFormView.fxml"));
            Parent root = loader.load();

            IngredientFormViewController controller = loader.getController();
            controller.setIngredient(ing);

            StackPane contentArea = (StackPane) ingredientsTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}