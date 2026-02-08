package org.proyectococina.service;

import org.proyectococina.domain.dto.WeeklyMenuDTO;
import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.domain.model.WeeklyMenu;
import org.proyectococina.domain.model.MenuItem;
import org.proyectococina.repository.WeeklyMenuRepository;
import org.proyectococina.repository.MenuItemRepository;
import org.proyectococina.repository.RecipeRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WeeklyMenuService implements IService<WeeklyMenuDTO, Long> {

    private final WeeklyMenuRepository menuRepo = new WeeklyMenuRepository();
    private final MenuItemRepository itemRepo = new MenuItemRepository();
    private final RecipeRepository recipeRepo = new RecipeRepository();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public List<WeeklyMenuDTO> findAll() {
        return menuRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WeeklyMenuDTO> findById(Long id) {
        return menuRepo.findById(id).map(this::mapToDTO);
    }

    @Override
    public void save(WeeklyMenuDTO dto) {
        WeeklyMenu entity = new WeeklyMenu();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStartDate(LocalDate.parse(dto.getStartDate(), dateFormatter));

        List<MenuItem> items = dto.getItems().stream()
                .filter(itemDto -> itemDto.getRecipeName() != null &&
                        !itemDto.getRecipeName().equals("--- FERIADO ---"))
                .map(itemDto -> {
                    MenuItem item = new MenuItem();
                    item.setDayOfWeek(itemDto.getDayOfWeek());
                    item.setMealTime(itemDto.getMealTime());

                    Long recipeId = recipeRepo.findByName(itemDto.getRecipeName())
                            .orElseThrow(() -> new RuntimeException("Recipe not found: " + itemDto.getRecipeName()))
                            .getId();
                    item.setRecipeId(recipeId);
                    return item;
                }).collect(Collectors.toList());

        menuRepo.saveFullMenu(entity, items);
    }

    @Override
    public void delete(WeeklyMenuDTO dto) {
        WeeklyMenu entity = new WeeklyMenu();
        entity.setId(dto.getId());
        menuRepo.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return menuRepo.existsById(id);
    }

    private WeeklyMenuDTO mapToDTO(WeeklyMenu menu) {
  
        List<MenuItemDTO> existingItems = itemRepo.findByMenuId(menu.getId()).stream().map(item -> {
            String recipeName = recipeRepo.findById(item.getRecipeId())
                    .map(r -> r.getName())
                    .orElse("--- FERIADO ---");

            return new MenuItemDTO(item.getId(), item.getRecipeId(), recipeName, item.getDayOfWeek(), item.getMealTime());
        }).collect(Collectors.toList());

        String[] days = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
        String[] meals = { "Almuerzo", "Cena" };
        List<MenuItemDTO> completeList = new ArrayList<>();

        for (String day : days) {
            for (String meal : meals) {
                MenuItemDTO coincidence = existingItems.stream()
                        .filter(i -> i.getDayOfWeek().equals(day) && i.getMealTime().equals(meal))
                        .findFirst()
                        .orElse(new MenuItemDTO(null, null, "--- FERIADO ---", day, meal)); 

                completeList.add(coincidence);
            }
        }

        return new WeeklyMenuDTO(
                menu.getId(),
                menu.getStartDate().format(dateFormatter),
                menu.getName(),
                completeList,
                menu.getInsertedAt() != null ? menu.getInsertedAt().format(dateTimeFormatter) : "N/A",
                menu.getUpdatedAt() != null ? menu.getUpdatedAt().format(dateTimeFormatter) : "N/A");
    }

    @Override
    public List<String> findAllNames() {
        return menuRepo.findAll().stream()
                .map(WeeklyMenu::getName)
                .collect(Collectors.toList());
    }
}