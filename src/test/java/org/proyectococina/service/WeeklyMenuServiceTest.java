package org.proyectococina.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.proyectococina.domain.dto.MenuItemDTO;
import org.proyectococina.domain.dto.WeeklyMenuDTO;
import org.proyectococina.domain.model.WeeklyMenu;
import org.proyectococina.repository.MenuItemRepository;
import org.proyectococina.repository.RecipeRepository;
import org.proyectococina.repository.WeeklyMenuRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeeklyMenuServiceTest {

    @Mock private WeeklyMenuRepository menuRepo;
    @Mock private MenuItemRepository itemRepo;
    @Mock private RecipeRepository recipeRepo;

    private WeeklyMenuService service;

    @BeforeEach
    void setUp() {
        service = new WeeklyMenuService(menuRepo, itemRepo, recipeRepo);
    }

    @Test
    void mapToDTO_ShouldAlwaysReturn14Items() {
        // GIVEN: Un menú sin ítems en la base de datos
        WeeklyMenu menu = new WeeklyMenu();
        menu.setId(1L);
        menu.setStartDate(LocalDate.now());
        menu.setName("Menú Invierno");

        when(menuRepo.findById(1L)).thenReturn(Optional.of(menu));
        when(itemRepo.findByMenuId(1L)).thenReturn(Collections.emptyList());

        // WHEN
        Optional<WeeklyMenuDTO> result = service.findById(1L);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(14, result.get().getItems().size(), "El menú semanal debe tener 14 turnos (7 días x 2 comidas)");
        assertEquals("--- FERIADO ---", result.get().getItems().get(0).getRecipeName());
    }

    @Test
    void save_ShouldThrowException_WhenRecipeNotFound() {
        // GIVEN: Un DTO con una receta inexistente
        MenuItemDTO item = new MenuItemDTO(null, null, "Receta Inexistente", "Lunes", "Almuerzo");
        WeeklyMenuDTO dto = new WeeklyMenuDTO(null, "01/03/2026", "Semana 1", List.of(item), null, null);

        when(recipeRepo.findByName("Receta Inexistente")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> service.save(dto));
        verify(menuRepo, never()).saveFullMenu(any(), any());
    }

    @Test
    void save_ShouldIgnoreFeriados_WhenPersisting() {
        // GIVEN: Un ítem marcado como feriado
        MenuItemDTO feriado = new MenuItemDTO(null, null, "--- FERIADO ---", "Lunes", "Almuerzo");
        WeeklyMenuDTO dto = new WeeklyMenuDTO(null, "01/03/2026", "Semana Feriada", List.of(feriado), null, null);

        // WHEN
        service.save(dto);

        // THEN: Se llama a saveFullMenu pero la lista de items debe estar vacía por el filtro
        verify(menuRepo).saveFullMenu(any(), argThat(List::isEmpty));
    }
}