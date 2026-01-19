package org.proyectococina.ui.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.proyectococina.domain.dto.WeeklyMenuDTO;
import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.domain.dto.ShoppingItemDTO;
import org.proyectococina.service.WeeklyMenuService;
import org.proyectococina.util.alert.ShowAlert;
import org.proyectococina.service.ShoppingListService;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaDeComprasViewController {

    @FXML private ComboBox<WeeklyMenuDTO> menuSelector;
    @FXML private VBox itemsContainer;
    @FXML private Button btnCalcular;

    private final WeeklyMenuService menuService = new WeeklyMenuService();
    private final ShoppingListService calculatorService = new ShoppingListService();
    private final Map<MenuItemDTO, Spinner<Integer>> servingInputs = new HashMap<>();

    @FXML
    public void initialize() {
        // Cargar los menús disponibles en el ComboBox
        menuSelector.getItems().addAll(menuService.findAll());
        
        // Listener para cuando cambie el menú seleccionado
        menuSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarPlatosDelMenu(newVal);
            }
        });
    }

    private void cargarPlatosDelMenu(WeeklyMenuDTO menu) {
        itemsContainer.getChildren().clear();
        servingInputs.clear();

        // Filtramos para que solo aparezcan platos que tienen una receta asignada
        menu.getItems().stream()
                .filter(item -> item.getRecipeName() != null && !item.getRecipeName().equals("--- FERIADO ---"))
                .forEach(item -> {
                    HBox row = new HBox(20);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPrefHeight(40);

                    Label label = new Label(String.format("%s %s: %s",
                            item.getDayOfWeek(), item.getMealTime(), item.getRecipeName()));
                    label.setPrefWidth(350);

                    // Selector numérico
                    Spinner<Integer> spinner = new Spinner<>(0, 100, 1);
                    spinner.setEditable(true);
                    spinner.setPrefWidth(80);

                    servingInputs.put(item, spinner);
                    row.getChildren().addAll(label, spinner);
                    itemsContainer.getChildren().add(row);
                });
    }

    @FXML
    private void onExportarLista() {
        if (servingInputs.isEmpty()) {
            ShowAlert.show("No hay un menú cargado.", Alert.AlertType.WARNING);
            return;
        }

        Map<MenuItemDTO, Integer> selections = new HashMap<>();
        servingInputs.forEach((item, spinner) -> {
            spinner.increment(0);
            int comensales = spinner.getValue();
            if (comensales > 0)
                selections.put(item, comensales);
        });

        if (selections.isEmpty()) {
            ShowAlert.show("Ingresá al menos 1 comensal.", Alert.AlertType.WARNING);
            return;
        }

        Map<String, List<ShoppingItemDTO>> resultado = calculatorService.calculate(selections);
        ejecutarExportacion(resultado);
    }

    private void ejecutarExportacion(Map<String, List<ShoppingItemDTO>> resultado) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Lista de Compras");
        fileChooser.setInitialFileName("Lista_Compras_" + LocalDate.now() + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto", "*.txt"));

        File file = fileChooser.showSaveDialog(itemsContainer.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new java.io.PrintWriter(file)) {
                escribirEncabezado(writer);
                resultado.forEach((proveedor, items) -> {
                    writer.println("PROVEEDOR: " + proveedor.toUpperCase());
                    writer.println("------------------------------------------");
                    for (ShoppingItemDTO item : items) {
                        // Mejora 2: Formateo de cantidades (quita .00 si es entero)
                        String cantStr = (item.totalAmount() == (long) item.totalAmount())
                                ? String.format("%d", (long) item.totalAmount())
                                : String.format("%.2f", item.totalAmount());

                        writer.printf("- %-25s : %s %s%n", item.ingredientName(), cantStr, item.unit());
                    }
                    writer.println();
                });
                ShowAlert.show("Guardado con éxito.", Alert.AlertType.INFORMATION);
            } catch (java.io.IOException e) {
                ShowAlert.show("Error: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    private void escribirEncabezado(java.io.PrintWriter writer) {
        
        String fechaActual = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        writer.println("==================================================");
        writer.println("           LISTA DE COMPRAS CALCULADA             ");
        writer.println("==================================================");
        writer.println(" Generada el: " + fechaActual);
        writer.println(" Nota: Cantidades basadas en comensales ingresados.");
        writer.println("==================================================\n");
    }
}
