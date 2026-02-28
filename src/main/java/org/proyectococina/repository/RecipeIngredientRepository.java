package org.proyectococina.repository;

import org.proyectococina.config.DataBaseConfig;
import org.proyectococina.domain.model.MeasurementUnit;
import org.proyectococina.domain.model.RecipeIngredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientRepository {

    private RecipeIngredient mapResultSetToEntity(ResultSet rs) throws SQLException {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipeId(rs.getLong("recipe_id"));
        ri.setIngredientId(rs.getLong("ingredient_id"));
        ri.setServingPerPerson(rs.getDouble("serving_per_person"));
        ri.setUnit(MeasurementUnit.valueOf(rs.getString("unit")));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) ri.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) ri.setUpdatedAt(updatedAt.toLocalDateTime());

        return ri;
    }

    public void save(RecipeIngredient entity) {
        String sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, serving_per_person, unit) VALUES (?, ?, ?, ?)";
        executeUpdate(sql, entity.getRecipeId(), entity.getIngredientId(), entity.getServingPerPerson(), entity.getUnit().name());
    }

    public void deleteAllByRecipeId(Long recipeId) {
        String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        executeUpdate(sql, recipeId);
    }

    public List<RecipeIngredient> findAllByRecipeId(Long recipeId) {
        String sql = "SELECT * FROM recipe_ingredients WHERE recipe_id = ?";
        List<RecipeIngredient> list = new ArrayList<>();
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, recipeId);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching ingredients for recipe: " + recipeId, e);
        }
        return list;
    }

    private void executeUpdate(String sql, Object... params) {
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update in RecipeIngredient", e);
        }
    }
}