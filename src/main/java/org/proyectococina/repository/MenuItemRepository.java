package org.proyectococina.repository;

import org.proyectococina.domain.model.MenuItem;
import org.proyectococina.config.DataBaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemRepository implements IRepository<MenuItem, Long> {

    @Override
    public MenuItem mapResultSetToEntity(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getLong("id"));
        item.setMenuId(rs.getLong("menu_id"));
        item.setRecipeId(rs.getLong("recipe_id"));
        item.setDayOfWeek(rs.getString("day_of_week"));
        item.setMealTime(rs.getString("meal_time"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        
        if (createdAt != null) item.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) item.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return item;
    }

    @Override
    public MenuItem save(MenuItem entity) {
        String sql = "INSERT INTO menu_items (menu_id, recipe_id, day_of_week, meal_time) VALUES (?, ?, ?, ?)";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, entity.getMenuId());
            pstmt.setLong(2, entity.getRecipeId());
            pstmt.setString(3, entity.getDayOfWeek());
            pstmt.setString(4, entity.getMealTime());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving menu item", e);
        }
    }

    @Override
    public MenuItem update(MenuItem entity) {
        String sql = "UPDATE menu_items SET recipe_id = ?, day_of_week = ?, meal_time = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entity.getRecipeId());
            pstmt.setString(2, entity.getDayOfWeek());
            pstmt.setString(3, entity.getMealTime());
            pstmt.setLong(4, entity.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("cant be updated: the menu item ID " + entity.getId() + " doesnt exist.");
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating menu item ID: " + entity.getId(), e);
        }
    }

    // Método extra muy útil para este repositorio específico
    public List<MenuItem> findByMenuId(Long menuId) {
        String sql = "SELECT * FROM menu_items WHERE menu_id = ?";
        List<MenuItem> items = new ArrayList<>();
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, menuId);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching items for menu ID: " + menuId, e);
        }
        return items;
    }

    @Override
    public void delete(MenuItem entity) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting menu item", e);
        }
    }

    @Override
    public List<MenuItem> findAll() {
        String sql = "SELECT * FROM menu_items";
        List<MenuItem> items = new ArrayList<>();
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                items.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all menu items", e);
        }
        return items;
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return getOne(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding menu item by ID: " + id, e);
        }
    }

    @Override
    public Optional<MenuItem> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> getOne(PreparedStatement pstmt) throws SQLException {
        try (var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM menu_items WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (var rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of menu item", e);
        }
    }
}