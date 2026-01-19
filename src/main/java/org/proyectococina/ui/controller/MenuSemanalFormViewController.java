package org.proyectococina.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.proyectococina.domain.dto.WeeklyMenuDTO;
import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.service.WeeklyMenuService;
import org.proyectococina.service.RecipeService;
import org.proyectococina.util.validators.MenuFormValidator;
import org.proyectococina.util.alert.ShowAlert;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MenuSemanalFormViewController {

    private static final String FERIADO_TAG = "--- FERIADO ---";
    private static final String[] DIAS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
    private static final String[] MOMENTOS = {"Almuerzo", "Cena"};

    @FXML private TextField nameField;
    @FXML private DatePicker startDatePicker;
    @FXML private GridPane weeklyGrid;
    @FXML private Label titleLabel;

    private final WeeklyMenuService menuService = new WeeklyMenuService();
    private final RecipeService recipeService = new RecipeService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private final Map<String, ComboBox<String>> menuSelectors = new HashMap<>();
    private Long currentId = null;

    @FXML
    public void initialize() {
        generarCalendarioBase();
    }

    private void generarCalendarioBase() {
        List<String> recetasDisponibles = recipeService.findAllNames();

        for (int i = 0; i < DIAS.length; i++) {
            weeklyGrid.add(new Label(DIAS[i]), i + 1, 0);
        }

        for (int m = 0; m < MOMENTOS.length; m++) {
            weeklyGrid.add(new Label(MOMENTOS[m]), 0, m + 1);
            
            for (int d = 0; d < DIAS.length; d++) {
                ComboBox<String> combo = createRecipeComboBox(recetasDisponibles);
                
                String key = DIAS[d] + "-" + MOMENTOS[m];
                menuSelectors.put(key, combo);
                
                weeklyGrid.add(combo, d + 1, m + 1);
            }
        }
    }

    private ComboBox<String> createRecipeComboBox(List<String> recetas) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(FERIADO_TAG);
        combo.getItems().addAll(recetas);
        combo.setPromptText("Elegir...");
        combo.setMaxWidth(Double.MAX_VALUE); 
        return combo;
    }

    public void setMenu(WeeklyMenuDTO menu) {
        if (menu == null) return;

        this.currentId = menu.getId();
        this.titleLabel.setText("Editar: " + menu.getName());
        this.nameField.setText(menu.getName());
        this.startDatePicker.setValue(LocalDate.parse(menu.getStartDate(), formatter));

        if (menu.getItems() != null) {
            menu.getItems().forEach(item -> {
                String key = item.getDayOfWeek() + "-" + item.getMealTime();
                ComboBox<String> combo = menuSelectors.get(key);
                if (combo != null) {
                    combo.setValue(item.getRecipeName());
                }
            });
        }
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) return;

        List<MenuItemDTO> items = new ArrayList<>();
        menuSelectors.forEach((key, combo) -> {
            String seleccion = combo.getValue();
            
            if (seleccion != null && !seleccion.equals(FERIADO_TAG)) {
                String[] parts = key.split("-");
                items.add(new MenuItemDTO(null, seleccion, parts[0], parts[1]));
            }
        });

        WeeklyMenuDTO dto = new WeeklyMenuDTO(  
                currentId,
                nameField.getText().trim(),
                startDatePicker.getValue().format(formatter),
                items
        );

        try {
            menuService.save(dto);
            ShowAlert.show("Menú semanal guardado exitosamente.", Alert.AlertType.INFORMATION);
            volverAlListado();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar el menú semanal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validarFormulario() {
        if (!MenuFormValidator.validateFields(nameField.getText(), startDatePicker.getValue())) {
            ShowAlert.show("Por favor, indique nombre y fecha.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (!MenuFormValidator.allCombosSelected(menuSelectors)) {
            ShowAlert.show("Por favor, marque todos los campos (use FERIADO si no aplica).", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    private void onCancelar() {
        volverAlListado();
    }

    private void volverAlListado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/MenuSemanalView.fxml"));
            Parent root = loader.load();
            StackPane contentArea = (StackPane) weeklyGrid.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(root);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la vista del listado de menús.");
            e.printStackTrace();
        }
    }
}