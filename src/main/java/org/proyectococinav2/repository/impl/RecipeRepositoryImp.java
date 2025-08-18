package org.proyectococinav2.repository.impl;

import org.proyectococinav2.config.DataBaseConfig;
import org.proyectococinav2.domain.model.Recipe;
import org.proyectococinav2.repository.RecipeRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeRepositoryImp implements RecipeRepository {

    @Override
    public Optional<Recipe> findRecipeByName(String name) {
        String sql = "SELECT * FROM RECIPE WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            return getRecipe(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe by name: " + name, e);
        }
    }

    @Override
    public Optional<Long> findRecipeIdByName(String name) {
        String sql = "SELECT ID FROM RECIPE WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("ID"));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe ID by name: " + name, e);
        }
    }

    @Override
    public void save(Recipe entity) {
        String sql = "INSERT INTO RECIPE (NAME, INSTRUCTIONS) VALUES (?, ?)";
         try (var connection = DataBaseConfig.getInstance().getConnection();
              var preparedStatement = connection.prepareStatement(sql)) {
             preparedStatement.setString(1, entity.getName());
             preparedStatement.setString(2, entity.getInstructions());
             preparedStatement.executeUpdate();
         } catch (Exception e) {
             throw new RuntimeException("Error saving recipe: " + entity, e);
         }
    }

    @Override
    public void update(Recipe entity) {
        String sql = "UPDATE RECIPE SET NAME = ?, INSTRUCTIONS = ? WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getInstructions());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe: " + entity, e);
        }
    }

    @Override
    public List<Recipe> findAll() {
        String sql = "SELECT * FROM RECIPE";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            List<Recipe> recipes = new ArrayList<>();
            while (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(resultSet.getLong("ID"));
                recipe.setName(resultSet.getString("NAME"));
                recipe.setInstructions(resultSet.getString("INSTRUCTIONS"));
                recipes.add(recipe);
            }
            return recipes;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all recipes", e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM RECIPE WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting recipe with ID: " + aLong, e);
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM RECIPE WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error checking existence of recipe with ID: " + aLong, e);
        }
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        String sql = "SELECT * FROM RECIPE WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            return getRecipe(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe by ID: " + id, e);
        }
    }

    private Optional<Recipe> getRecipe(PreparedStatement preparedStatement) throws SQLException {
        var resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Recipe recipe = new Recipe();
            recipe.setId(resultSet.getLong("ID"));
            recipe.setName(resultSet.getString("NAME"));
            recipe.setInstructions(resultSet.getString("INSTRUCTIONS"));
            return Optional.of(recipe);
        }
        return Optional.empty();
    }
}
