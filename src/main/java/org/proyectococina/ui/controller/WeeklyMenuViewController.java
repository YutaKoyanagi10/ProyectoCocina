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
import org.proyectococina.domain.dto.WeeklyMenuDTO;
import org.proyectococina.service.WeeklyMenuService;

import java.io.IOException;

public class WeeklyMenuViewController {

    @FXML private TableView<WeeklyMenuDTO> menuTable;
    @FXML private TableColumn<WeeklyMenuDTO, Long> idColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> nameColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> startDateColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> insertedAtColumn; 
    @FXML private TableColumn<WeeklyMenuDTO, String> updatedAtColumn; 
    @FXML private TableColumn<WeeklyMenuDTO, Void> actionsColumn;
    @FXML private TextField searchField;

    private final ObservableList<WeeklyMenuDTO> masterData = FXCollections.observableArrayList();
    private final WeeklyMenuService menuService = new WeeklyMenuService();

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
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        insertedAtColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltering() {
        FilteredList<WeeklyMenuDTO> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(menu -> {
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();
                
                return (menu.getName() != null && menu.getName().toLowerCase().contains(filter)) ||
                       (menu.getStartDate() != null && menu.getStartDate().contains(filter));
            });
        });

        SortedList<WeeklyMenuDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(menuTable.comparatorProperty());
        menuTable.setItems(sortedData);
    }

    private void loadData() {
        masterData.setAll(menuService.findAll());
    }

    private void configureActions() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button("Ver");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox box = new HBox(10, viewButton, deleteButton);

            {
                box.setAlignment(Pos.CENTER);
                viewButton.setOnAction(event -> viewMenu(getTableRow().getItem()));
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

    private void confirmDeletion(WeeklyMenuDTO menu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar el menú semanal '" + menu.getName() + "'?", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                menuService.delete(menu);
                loadData();
            }
        });
    }

    private void viewMenu(WeeklyMenuDTO menu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/WeeklyMenuFormView.fxml"));
            Parent root = loader.load();
            WeeklyMenuFormViewController controller = loader.getController();
            controller.setMenu(menu);

            StackPane contentArea = (StackPane) menuTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar WeeklyMenuFormView.fxml");
            e.printStackTrace();
        }
    }
}