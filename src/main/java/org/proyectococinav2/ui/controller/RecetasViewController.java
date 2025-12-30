package org.proyectococinav2.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.proyectococinav2.domain.dto.RecipeDTO;
import org.proyectococinav2.domain.mapper.RecipeMapper;
import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.repository.impl.RecipeRepositoryImp;
import org.proyectococinav2.service.RecipeService;
import org.proyectococinav2.service.impl.RecipeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

import java.io.IOException;

import javafx.scene.control.ButtonType;

public class RecetasViewController extends Controller {
    @FXML
    private TableView<RecipeDTO> recetasTable;
    @FXML
    private TableColumn<RecipeDTO, Long> idColumn;
    @FXML
    private TableColumn<RecipeDTO, String> nombreColumn;
    @FXML
    private TableColumn<RecipeDTO, String> instruccionColumn;
    @FXML
    private TableColumn<RecipeDTO, Void> accionesColumn;

    private final RecipeService recipeService = new RecipeServiceImpl(new RecipeRepositoryImp());

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        instruccionColumn.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        ObservableList<RecipeDTO> recetas = FXCollections.observableArrayList();
        for (Recipe recipe : recipeService.findAll()) {
            recetas.add(RecipeMapper.toDTO(recipe));
        }
        recetasTable.setItems(recetas);
        // Agregar columna de acciones
        accionesColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("Ver");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnVer.setOnAction(e -> {
                    RecipeDTO receta = getTableView().getItems().get(getIndex());
                    verReceta(receta);
                });
                btnEliminar.setOnAction(e -> {
                    RecipeDTO receta = getTableView().getItems().get(getIndex());
                    eliminarReceta(receta);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnVer, btnEliminar);
                    setGraphic(box);
                }
            }
        });
    }

    private void verReceta(RecipeDTO receta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococinav2/ui/view/RecetaFormView.fxml"));
            Parent root = loader.load();

            RecetaFormViewController controller = loader.getController();
            // Pasar la escena actual para poder volver
            controller.setMenuScene(recetasTable.getScene());
            controller.setRecipe(receta);

            // Cambiar la escena principal en vez de abrir una nueva ventana
            Stage stage = (Stage) recetasTable.getScene().getWindow();
            stage.setTitle("Ver Receta");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarReceta(RecipeDTO receta) {
        // Confirmar eliminación
        Alert alert = new Alert(CONFIRMATION, "¿Seguro que deseas eliminar la receta '" + receta.getName() + "'?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Eliminar de la base de datos
                recipeService.deleteById(receta.getId());
                // Eliminar de la tabla
                recetasTable.getItems().remove(receta);
                Alert info = new Alert(INFORMATION, "Receta eliminada correctamente.", ButtonType.OK);
                info.setHeaderText(null);
                info.showAndWait();
            }
        });
    }
    @FXML
    private void onCancel() {
        volverAlMenuPrincipal(recetasTable.getScene());
    }
}
