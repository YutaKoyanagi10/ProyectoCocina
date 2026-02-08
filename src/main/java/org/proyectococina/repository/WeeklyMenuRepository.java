package org.proyectococina.repository;

import org.proyectococina.config.DataBaseConfig;
import org.proyectococina.domain.model.WeeklyMenu;
import org.proyectococina.domain.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeeklyMenuRepository implements IRepository<WeeklyMenu, Long> {

    @Override
    public WeeklyMenu mapResultSetToEntity(ResultSet rs) throws SQLException {
        WeeklyMenu menu = new WeeklyMenu(
            rs.getLong("id"),
            rs.getDate("start_date").toLocalDate(),
            rs.getString("name")
        );
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        
        if (createdAt != null) menu.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) menu.setUpdatedAt(updatedAt.toLocalDateTime());
        return menu;
    }

    public void saveFullMenu(WeeklyMenu menu, List<MenuItem> items) {
        String sqlMenuInsert = "INSERT INTO weekly_menus (start_date, name) VALUES (?, ?)";
        String sqlMenuUpdate = "UPDATE weekly_menus SET start_date = ?, name = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        String sqlItemDelete = "DELETE FROM menu_items WHERE menu_id = ?";
        String sqlItemInsert = "INSERT INTO menu_items (menu_id, recipe_id, day_of_week, meal_time) VALUES (?, ?, ?, ?)";

        try (var conn = DataBaseConfig.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                long menuId;
                if (menu.getId() == null) {
                    try (var pstmtMenu = conn.prepareStatement(sqlMenuInsert, Statement.RETURN_GENERATED_KEYS)) {
                        pstmtMenu.setDate(1, Date.valueOf(menu.getStartDate()));
                        pstmtMenu.setString(2, menu.getName());
                        pstmtMenu.executeUpdate();
                        var rs = pstmtMenu.getGeneratedKeys();
                        if (!rs.next())
                            throw new SQLException("Error al obtener ID del menú");
                        menuId = rs.getLong(1);
                    }
                } else {
                    menuId = menu.getId();
                    try (var pstmtMenu = conn.prepareStatement(sqlMenuUpdate)) {
                        pstmtMenu.setDate(1, Date.valueOf(menu.getStartDate()));
                        pstmtMenu.setString(2, menu.getName());
                        pstmtMenu.setLong(3, menuId);
                        pstmtMenu.executeUpdate();
                    }
                    try (var pstmtDelete = conn.prepareStatement(sqlItemDelete)) {
                        pstmtDelete.setLong(1, menuId);
                        pstmtDelete.executeUpdate();
                    }
                }

                try (var pstmtItem = conn.prepareStatement(sqlItemInsert)) {
                    for (MenuItem item : items) {
                        pstmtItem.setLong(1, menuId);
                        pstmtItem.setLong(2, item.getRecipeId());
                        pstmtItem.setString(3, item.getDayOfWeek());
                        pstmtItem.setString(4, item.getMealTime());
                        pstmtItem.addBatch();
                    }
                    pstmtItem.executeBatch();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing full weekly menu", e);
        }
    }

    @Override
    public void save(WeeklyMenu entity) {
        String sql = "INSERT INTO weekly_menus (start_date, name) VALUES (?, ?)";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, Date.valueOf(entity.getStartDate()));
            pstmt.setString(2, entity.getName());
            pstmt.executeUpdate();
            var rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving WeeklyMenu", e);
        }
    }
    @Override
    public void update(WeeklyMenu entity) {
        String sql = "UPDATE weekly_menus SET start_date = ?, name = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(entity.getStartDate()));
            pstmt.setString(2, entity.getName());
            pstmt.setLong(3, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating WeeklyMenu", e);
        }
    }
    @Override
    public void delete(WeeklyMenu entity) {
        String sql = "DELETE FROM weekly_menus WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting WeeklyMenu", e);
        }
    }
    @Override
    public List<WeeklyMenu> findAll() {
        List<WeeklyMenu> list = new ArrayList<>();
        String sql = "SELECT * FROM weekly_menus";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all WeeklyMenus", e);
        }
        return list;
    }
    @Override
    public Optional<WeeklyMenu> findById(Long id) {
        String sql = "SELECT * FROM weekly_menus WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding WeeklyMenu by id", e);
        }
        return Optional.empty();
    }
    @Override
    public Optional<WeeklyMenu> findByName(String name) {
        String sql = "SELECT * FROM weekly_menus WHERE name = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding WeeklyMenu by name", e);
        }
        return Optional.empty();
    }
    @Override
    public Optional<WeeklyMenu> getOne(PreparedStatement pstmt) throws SQLException {
        try (var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT 1 FROM weekly_menus WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (var rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of WeeklyMenu", e);
        }
    }
}