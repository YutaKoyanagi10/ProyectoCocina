package org.proyectococinav2.repository.impl;

import org.proyectococinav2.config.DataBaseConfig;
import org.proyectococinav2.domain.model.MeasurementUnit;
import org.proyectococinav2.domain.model.RecipeIngredient;
import org.proyectococinav2.repository.RecipeIngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeIngredientRepositoryImpl implements RecipeIngredientRepository {
    @Override
    public List<RecipeIngredient> findAllByRecipeId(Long Id) {
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ?";
        try {var connection = DataBaseConfig.getInstance().getConnection();
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, Id);
            var resultSet = preparedStatement.executeQuery();
            return recipeIngredients(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recipe ingredients by recipe ID: " + Id, e);
        }
    }

    @Override
    public List<RecipeIngredient> findAllByIngredientId(Long Id) {
        String sql = "SELECT * FROM recipe_ingredient WHERE ingredient_id = ?";
        try {var connection = DataBaseConfig.getInstance().getConnection();
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, Id);
            var resultSet = preparedStatement.executeQuery();
            return recipeIngredients(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recipe ingredients by ingredient ID: " + Id, e);
        }
    }

    private List<RecipeIngredient> recipeIngredients(ResultSet resultSet) throws SQLException {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        while (resultSet.next()) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipeId(resultSet.getLong("recipe_id"));
            recipeIngredient.setIngredientId(resultSet.getLong("ingredient_id"));
            recipeIngredient.setServingPerPerson(resultSet.getInt("serving_per_person"));
            recipeIngredient.setUnit(MeasurementUnit.valueOf(resultSet.getString("unit")));
            recipeIngredients.add(recipeIngredient);
        }
        return recipeIngredients;
    }
    @Override
    public void save(RecipeIngredient entity) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, serving_per_person, unit) VALUES (?, ?, ?, ?)";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getRecipeId());
            preparedStatement.setLong(2, entity.getIngredientId());
            preparedStatement.setDouble(3, entity.getServingPerPerson());
            preparedStatement.setString(4, entity.getUnit().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving recipe ingredient: " + entity, e);
        }
    }

    @Override
    public void update(RecipeIngredient entity) {
        String sql = "UPDATE recipe_ingredient SET recipe_id = ?, ingredient_id = ?, serving_per_person = ?, unit = ? WHERE recipe_id = ? AND ingredient_id = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getRecipeId());
            preparedStatement.setLong(2, entity.getIngredientId());
            preparedStatement.setDouble(3, entity.getServingPerPerson());
            preparedStatement.setString(4, entity.getUnit().name());
            preparedStatement.setLong(5, entity.getRecipeId());
            preparedStatement.setLong(6, entity.getIngredientId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating recipe ingredient: " + entity, e);
        }
    }

    @Override
    public List<RecipeIngredient> findAll() {
        String sql = "SELECT * FROM recipe_ingredient";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            return recipeIngredients(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all recipe ingredients", e);
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, recipeId);
            preparedStatement.setLong(2, ingredientId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el ingrediente de la receta", e);
        }
    }

    @Override
    public boolean existsById(Long recipeId, Long ingredientId) {
        String sql = "SELECT 1 FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, recipeId);
            preparedStatement.setLong(2, ingredientId);
            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia", e);
        }
    }

    @Override
    public Optional<RecipeIngredient> findById(Long recipeId, Long ingredientId) {
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, recipeId);
            preparedStatement.setLong(2, ingredientId);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setRecipeId(resultSet.getLong("recipe_id"));
                    recipeIngredient.setIngredientId(resultSet.getLong("ingredient_id"));
                    recipeIngredient.setServingPerPerson(resultSet.getInt("serving_per_person"));
                    recipeIngredient.setUnit(MeasurementUnit.valueOf(resultSet.getString("unit")));
                    return Optional.of(recipeIngredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding recipe ingredient by ID", e);
        }
        return Optional.empty();
    }
}
