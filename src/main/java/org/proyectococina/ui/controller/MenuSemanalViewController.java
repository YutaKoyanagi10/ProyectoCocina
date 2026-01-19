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

public class MenuSemanalViewController {

    @FXML private TableView<WeeklyMenuDTO> menuTable;
    @FXML private TableColumn<WeeklyMenuDTO, Long> idColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> nombreColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> fechaInicioColumn;
    @FXML private TableColumn<WeeklyMenuDTO, String> fechaAltaColumn; 
    @FXML private TableColumn<WeeklyMenuDTO, String> fechaModColumn; 
    @FXML private TableColumn<WeeklyMenuDTO, Void> accionesColumn;
    @FXML private TextField searchField;

    private final ObservableList<WeeklyMenuDTO> masterData = FXCollections.observableArrayList();
    private final WeeklyMenuService menuService = new WeeklyMenuService();

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
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        fechaAltaColumn.setCellValueFactory(new PropertyValueFactory<>("insertedAt"));
        fechaModColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void setupFiltrado() {
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

    private void cargarDatos() {
        masterData.setAll(menuService.findAll());
    }

    private void configurarAcciones() {
        accionesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(10, btnVer, btnEliminar);

            {
                box.setAlignment(Pos.CENTER);
                btnVer.setOnAction(event -> verMenu(getTableRow().getItem()));
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

    private void confirmarEliminacion(WeeklyMenuDTO menu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
            "¿Deseas eliminar el menú semanal '" + menu.getName() + "'?", 
            ButtonType.YES, ButtonType.NO);
            
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                menuService.delete(menu);
                cargarDatos();
            }
        });
    }

    private void verMenu(WeeklyMenuDTO menu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/MenuSemanalFormView.fxml"));
            Parent root = loader.load();
            MenuSemanalFormViewController controller = loader.getController();
            controller.setMenu(menu);

            StackPane contentArea = (StackPane) menuTable.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar MenuSemanalFormView.fxml");
            e.printStackTrace();
        }
    }
}