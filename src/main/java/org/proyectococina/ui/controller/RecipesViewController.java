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
import org.proyectococina.domain.dto.RecipeDTO;
import org.proyectococina.service.RecipeService;

import java.io.IOException;

public class RecipesViewController {

    @FXML private TableView<RecipeDTO> recipesTable;
    @FXML private TableColumn<RecipeDTO, Long> idColumn;
    @FXML private TableColumn<RecipeDTO, String> nameColumn;
    @FXML private TableColumn<RecipeDTO, String> instructionColumn;
    @FXML private TableColumn<RecipeDTO, Void> actionsColumn;
    @FXML private TableColumn<RecipeDTO, String> insertedAtColumn;
    @FXML private TableColumn<RecipeDTO, String> updatedAtColumn;
    @FXML private TextField searchField;

    private final ObservableList<RecipeDTO> masterData = FXCollections.observableArrayList();

    private final RecipeService recipeService = new RecipeService(); 

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
        instructionColumn.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        insertedAtColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltering() {
        FilteredList<RecipeDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(recipe -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();
                
                return (recipe.getName() != null && recipe.getName().toLowerCase().contains(filter)) ||
                       (recipe.getInstructions() != null && recipe.getInstructions().toLowerCase().contains(filter));
            });
        });

        SortedList<RecipeDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(recipesTable.comparatorProperty());
        recipesTable.setItems(sortedData);
    }

    private void loadData() {
        masterData.setAll(recipeService.findAll());
    }

    private void configureActions() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button("Ver");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox container = new HBox(10, viewButton, deleteButton);

            {
                container.setAlignment(Pos.CENTER);
                viewButton.setOnAction(e -> viewRecipe(getTableRow().getItem()));
                deleteButton.setOnAction(e -> deleteRecipe(getTableRow().getItem()));
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

    private void viewRecipe(RecipeDTO recipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/RecipeFormView.fxml"));
            Parent root = loader.load();

            RecipeFormViewController controller = loader.getController();
            controller.setRecipe(recipe);

            StackPane contentArea = (StackPane) recipesTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecipe(RecipeDTO recipe) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Seguro que deseas eliminar la receta '" + recipe.getName() + "'?", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                recipeService.delete(recipe);
                loadData();
            }
        });
    }
}