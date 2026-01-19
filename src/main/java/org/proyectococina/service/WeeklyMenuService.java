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
                            .orElseThrow(() -> new RuntimeException("Receta no encontrada: " + itemDto.getRecipeName()))
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
        // 1. Obtenemos los items que SÍ están en la DB
        List<MenuItemDTO> itemsExistentes = itemRepo.findByMenuId(menu.getId()).stream().map(item -> {
            String recipeName = recipeRepo.findById(item.getRecipeId())
                    .map(r -> r.getName())
                    .orElse("--- FERIADO ---");

            return new MenuItemDTO(item.getId(), item.getRecipeId(), recipeName, item.getDayOfWeek(), item.getMealTime());
        }).collect(Collectors.toList());

        // 2. Creamos una lista completa con los 14 espacios (7 días x 2 momentos)
        String[] dias = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
        String[] momentos = { "Almuerzo", "Cena" };
        List<MenuItemDTO> listaCompleta = new ArrayList<>();

        for (String dia : dias) {
            for (String momento : momentos) {
                // Buscamos si existe en los datos traídos de la DB
                MenuItemDTO coincidencia = itemsExistentes.stream()
                        .filter(i -> i.getDayOfWeek().equals(dia) && i.getMealTime().equals(momento))
                        .findFirst()
                        .orElse(new MenuItemDTO(null, null, "--- FERIADO ---", dia, momento)); // Si no está, es Feriado

                listaCompleta.add(coincidencia);
            }
        }

        return new WeeklyMenuDTO(
                menu.getId(),
                menu.getStartDate().format(dateFormatter),
                menu.getName(),
                listaCompleta, // Enviamos la lista con los 14 elementos
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