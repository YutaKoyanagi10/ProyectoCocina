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

public class WeeklyMenuFormViewController {

    private static final String HOLIDAY_TAG = "--- FERIADO ---";
    private static final String[] DAYS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
    private static final String[] MEALS = {"Almuerzo", "Cena"};

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
        generateBaseCalendar();
    }

    private void generateBaseCalendar() {
        List<String> availableRecipes = recipeService.findAllNames();

        for (int i = 0; i < DAYS.length; i++) {
            weeklyGrid.add(new Label(DAYS[i]), i + 1, 0);
        }

        for (int m = 0; m < MEALS.length; m++) {
            weeklyGrid.add(new Label(MEALS[m]), 0, m + 1);
            
            for (int d = 0; d < DAYS.length; d++) {
                ComboBox<String> combo = createRecipeComboBox(availableRecipes);
                
                String key = DAYS[d] + "-" + MEALS[m];
                menuSelectors.put(key, combo);
                
                weeklyGrid.add(combo, d + 1, m + 1);
            }
        }
    }

    private ComboBox<String> createRecipeComboBox(List<String> recipes) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(HOLIDAY_TAG);
        combo.getItems().addAll(recipes);
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
    private void onSave() {
        if (!validateForm()) return;

        List<MenuItemDTO> items = new ArrayList<>();
        menuSelectors.forEach((key, combo) -> {
            String selection = combo.getValue();
            
            if (selection != null && !selection.equals(HOLIDAY_TAG)) {
                String[] parts = key.split("-");
                items.add(new MenuItemDTO(null, null, selection, parts[0], parts[1]));
            }
        });

        WeeklyMenuDTO dto = new WeeklyMenuDTO(  
                currentId,
                startDatePicker.getValue().format(formatter),
                nameField.getText().trim(),
                items
        );

        try {
            menuService.save(dto);
            ShowAlert.show("Menú semanal guardado exitosamente.", Alert.AlertType.INFORMATION);
            returnToList();
        } catch (Exception e) {
            ShowAlert.show("Error al guardar el menú semanal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateForm() {
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
    private void onCancel() {
        returnToList();
    }

    private void returnToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectococina/ui/view/WeeklyMenuView.fxml"));
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