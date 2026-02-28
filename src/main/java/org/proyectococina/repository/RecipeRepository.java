package org.proyectococina.repository;

import org.proyectococina.config.DataBaseConfig;
import org.proyectococina.domain.model.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeRepository implements IRepository<Recipe, Long> {

    @Override
    public Recipe mapResultSetToEntity(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getLong("ID"));
        recipe.setName(rs.getString("NAME"));
        recipe.setInstructions(rs.getString("INSTRUCTIONS"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
    
        if (createdAt != null) recipe.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) recipe.setUpdatedAt(updatedAt.toLocalDateTime());
    
        return recipe;
    }

    @Override
    public Optional<Recipe> findByName(String name) {
        String sql = "SELECT * FROM RECIPES WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            return getOne(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe by name: " + name, e);
        }
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        String sql = "SELECT * FROM RECIPES WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            return getOne(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding recipe by ID: " + id, e);
        }
    }

    @Override
    public Recipe save(Recipe entity) {
        String sql = "INSERT INTO RECIPES (NAME, INSTRUCTIONS) VALUES (?, ?)";
        try (var connection = DataBaseConfig.getInstance().getConnection();
            var preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getInstructions());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error saving recipe: " + entity, e);
        }
    }

    @Override
    public Recipe update(Recipe entity) {
        String sql = "UPDATE RECIPES SET NAME = ?, INSTRUCTIONS = ?, updated_at = CURRENT_TIMESTAMP WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
            var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getInstructions());
            preparedStatement.setLong(3, entity.getId());
            int rowsAffected = preparedStatement.executeUpdate();
        
            if (rowsAffected == 0) {
                throw new RuntimeException("cant be updated: the recipe ID " + entity.getId() + " doesnt exist.");
            }
        
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe", e);
        }
    }

    @Override
    public List<Recipe> findAll() {
        String sql = "SELECT * FROM RECIPES";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            List<Recipe> recipes = new ArrayList<>();
            while (resultSet.next()) {
                recipes.add(mapResultSetToEntity(resultSet));
            }
            return recipes;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all recipes", e);
        }
    }

    @Override
    public void delete(Recipe entity) {
        String sql = "DELETE FROM RECIPES WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting recipe with ID: " + entity.getId(), e);
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM RECIPES WHERE ID = ?";
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
    public Optional<Recipe> getOne(PreparedStatement preparedStatement) throws SQLException {
        var resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(mapResultSetToEntity(resultSet));
        }
        return Optional.empty();
    }
}
