package org.proyectococinav2.service;

import org.proyectococinav2.domain.dto.RecipeDTO;
import org.proyectococinav2.domain.dto.RecipeIngredientDTO;
import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.repository.RecipeRepository;
import org.proyectococinav2.repository.RecipeIngredientRepository;
import org.proyectococinav2.repository.IngredientRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeService implements IService<RecipeDTO, Long> {

    private final RecipeRepository recipeRepo = new RecipeRepository();
    private final RecipeIngredientRepository riRepo = new RecipeIngredientRepository();
    private final IngredientRepository ingredientRepo = new IngredientRepository();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeDTO> findById(Long id) {
        return recipeRepo.findById(id).map(this::mapToDTO);
    }

    @Override
    public void save(RecipeDTO dto) {
        Recipe entity = new Recipe();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setInstructions(dto.getInstructions());

        if (entity.getId() == null) {
            recipeRepo.save(entity);
            Recipe saved = recipeRepo.findByName(entity.getName())
                    .orElseThrow(() -> new RuntimeException("Error al recuperar receta recién guardada"));
            saveIngredientsInternal(saved.getId(), dto.getIngredients());
        } else {
            recipeRepo.update(entity);
            riRepo.deleteAllByRecipeId(entity.getId());
            saveIngredientsInternal(entity.getId(), dto.getIngredients());
        }
    }

    @Override
    public void delete(RecipeDTO dto) {
        Recipe entity = new Recipe();
        entity.setId(dto.getId());
        recipeRepo.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return recipeRepo.existsById(id);
    }

    private RecipeDTO mapToDTO(Recipe recipe) {
        // 1. Obtener las relaciones intermedias
        List<RecipeIngredient> relations = riRepo.findAllByRecipeId(recipe.getId());

        // 2. Mapear relaciones a RecipeIngredientDTO (buscando nombres de ingredientes)
        List<RecipeIngredientDTO> ingredientDTOs = relations.stream().map(ri -> {
            String ingName = ingredientRepo.findById(ri.getIngredientId())
                    .map(ing -> ing.getName())
                    .orElse("Desconocido");
            
            return new RecipeIngredientDTO(
                ingName,
                ri.getServingPerPerson(),
                ri.getUnit()
            );
        }).collect(Collectors.toList());

        // 3. Construir el RecipeDTO (asegúrate de que el constructor coincida)
        return new RecipeDTO(
            recipe.getId(),
            recipe.getName(),
            recipe.getInstructions(),
            ingredientDTOs,
            recipe.getInsertedAt() != null ? recipe.getInsertedAt().format(formatter) : "N/A",
            recipe.getUpdatedAt() != null ? recipe.getUpdatedAt().format(formatter) : "N/A"
        );
    }

    private void saveIngredientsInternal(Long recipeId, List<RecipeIngredientDTO> ingredients) {
        for (RecipeIngredientDTO ingDto : ingredients) {
            Long ingId = ingredientRepo.findByName(ingDto.getIngredientName())
                    .orElseThrow(() -> new RuntimeException("No existe el ingrediente: " + ingDto.getIngredientName()))
                    .getId();

            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipeId(recipeId);
            ri.setIngredientId(ingId);
            ri.setServingPerPerson(ingDto.getQuantity());
            ri.setUnit(ingDto.getUnit());

            riRepo.save(ri);
        }
    }

    @Override
    public List<String> findAllNames() {
        return recipeRepo.findAll().stream()
                .map(Recipe::getName)
                .collect(Collectors.toList());
    }
}