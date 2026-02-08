package org.proyectococina.service;

import org.proyectococina.domain.dto.RecipeDTO;
import org.proyectococina.domain.dto.RecipeIngredientDTO;
import org.proyectococina.domain.model.Recipe;
import org.proyectococina.domain.model.RecipeIngredient;
import org.proyectococina.repository.RecipeRepository;
import org.proyectococina.repository.RecipeIngredientRepository;
import org.proyectococina.repository.IngredientRepository;

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
                    .orElseThrow(() -> new RuntimeException("Error recovering saved recipe: " + entity.getName()));
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
        List<RecipeIngredient> relations = riRepo.findAllByRecipeId(recipe.getId());

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
                    .orElseThrow(() -> new RuntimeException("Ingredient does not exist: " + ingDto.getIngredientName()))
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