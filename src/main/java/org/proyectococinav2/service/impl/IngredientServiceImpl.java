package org.proyectococinav2.service.impl;

import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.repository.IngredientRepository;
import org.proyectococinav2.service.IngredientService;

import java.util.List;
import java.util.Optional;

public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return Optional.ofNullable(ingredientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ingredient with id " + id + " does not exist.")));
    }

    @Override
    public void save(Ingredient ingredient) {
        if (ingredientRepository.existsById(ingredient.getId())) {
            ingredientRepository.update(ingredient);
        }else{
            ingredientRepository.save(ingredient);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Ingredient with id " + id + " does not exist.");
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        return ingredientRepository.existsById(id);
    }

    @Override
    public List<Ingredient> findAllBySupplierId(Long supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID cannot be null.");
        }
        return ingredientRepository.findAllBySupplierId(supplierId);
    }

    @Override
    public Optional<Ingredient> findIngredientByName(String name) {
        return ingredientRepository.findIngredientByName(name);
    }

    @Override
    public Optional<Long> findIngredientIdByName(String name) {
        return ingredientRepository.findIngredientIdByName(name);
    }
    @Override
    public List<String> findAllNames() {
        return ingredientRepository.findAllNames();
    }
}
