package org.proyectococina.repository;

import org.proyectococina.domain.model.Ingredient;
import org.proyectococina.config.DataBaseConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class IngredientRepository implements IRepository<Ingredient, Long> {

    @Override
    public Ingredient mapResultSetToEntity(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setSupplierId(rs.getLong("supplier_id"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        
        if (createdAt != null) ingredient.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) ingredient.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return ingredient;
    }

    @Override
    public Ingredient save(Ingredient entity) {
        String sql = "INSERT INTO ingredients (name, supplier_id) VALUES (?, ?)";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entity.getName());
            pstmt.setLong(2, entity.getSupplierId());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving ingredient: " + entity.getName(), e);
        }
    }

    @Override
    public Ingredient update(Ingredient entity) {
        String sql = "UPDATE ingredients SET name = ?, supplier_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setLong(2, entity.getSupplierId());
            pstmt.setLong(3, entity.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("cant be updated: the ingredient ID " + entity.getId() + " doesnt exist.");
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating ingredient ID: " + entity.getId(), e);
        }
    }

    @Override
    public void delete(Ingredient entity) {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ingredient ID: " + entity.getId(), e);
        }
    }

    @Override
    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM ingredients";
        List<Ingredient> ingredients = new ArrayList<>();
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ingredients.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all ingredients", e);
        }
        return ingredients;
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        String sql = "SELECT * FROM ingredients WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return getOne(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding ingredient by ID: " + id, e);
        }
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        String sql = "SELECT * FROM ingredients WHERE name = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return getOne(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding ingredient by name: " + name, e);
        }
    }

    @Override
    public Optional<Ingredient> getOne(PreparedStatement pstmt) throws SQLException {
        try (var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM ingredients WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (var rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of ingredient ID: " + id, e);
        }
    }
}