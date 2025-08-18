package org.proyectococinav2.repository.impl;

import org.proyectococinav2.config.DataBaseConfig;
import org.proyectococinav2.domain.model.Ingredient;
import org.proyectococinav2.repository.IngredientRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientRepositoryImpl implements IngredientRepository {
    @Override
    public List<Ingredient> findAllBySupplierId(Long supplierId) {
        String sql = "SELECT * FROM INGREDIENT WHERE SUPPLIER_ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, supplierId);
            try (var resultSet = preparedStatement.executeQuery()) {
                return getIngredientsList(resultSet);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding ingredients by supplier ID: " + supplierId, e);
        }
    }

    private List<Ingredient> getIngredientsList(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        while (resultSet.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(resultSet.getLong("ID"));
            ingredient.setName(resultSet.getString("NAME"));
            ingredient.setSupplierId(resultSet.getLong("SUPPLIER_ID"));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    @Override
    public Optional<Ingredient> findIngredientByName(String name) {
        String sql = "SELECT * FROM INGREDIENT WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            return getIngredient(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding ingredient by name: " + name, e);
        }
    }

    @Override
    public Optional<Long> findIngredientIdByName(String name) {
        String sql = "SELECT ID FROM INGREDIENT WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getLong("ID"));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding ingredient ID by name: " + name, e);
        }
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        String sql = "SELECT * FROM INGREDIENT WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            return getIngredient(preparedStatement);
        } catch (Exception e) {
            throw new RuntimeException("Error finding ingredient by ID: " + id, e);
        }
    }

    private Optional<Ingredient> getIngredient(PreparedStatement preparedStatement) throws SQLException {
        try (var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getLong("ID"));
                ingredient.setName(resultSet.getString("NAME"));
                ingredient.setSupplierId(resultSet.getLong("SUPPLIER_ID"));
                return Optional.of(ingredient);
            }
            return Optional.empty();
        }
    }

    @Override
    public void save(Ingredient entity) {
        String sql = "INSERT INTO INGREDIENT (NAME, SUPPLIER_ID) VALUES (?, ?)";
         try (var connection = DataBaseConfig.getInstance().getConnection();
              var preparedStatement = connection.prepareStatement(sql)) {
             preparedStatement.setString(1, entity.getName());
             preparedStatement.setLong(2, entity.getSupplierId());
             preparedStatement.executeUpdate();
         } catch (Exception e) {
             throw new RuntimeException("Error saving ingredient: " + entity, e);
         }
    }

    @Override
    public void update(Ingredient entity) {
        String sql = "UPDATE INGREDIENT SET NAME = ?, SUPPLIER_ID = ? WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getSupplierId());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error updating ingredient: " + entity, e);
        }
    }

    @Override
    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM INGREDIENT";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            return getIngredientsList(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Error finding all ingredients", e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM INGREDIENT WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting ingredient by ID: " + aLong, e);
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM INGREDIENT WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking existence of ingredient by ID: " + aLong, e);
        }
    }
    @Override
    public List<String> findAllNames() {
        String sql = "SELECT NAME FROM INGREDIENT";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("NAME"));
            }
            return names;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all ingredient names", e);
        }
    }
}
