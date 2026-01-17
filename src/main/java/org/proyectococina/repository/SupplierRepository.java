package org.proyectococina.repository;

import org.proyectococina.config.DataBaseConfig;
import org.proyectococina.domain.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierRepository implements IRepository<Supplier, Long> {

    @Override
    public Supplier mapResultSetToEntity(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("id"));
        supplier.setName(rs.getString("name"));
        supplier.setContactInfo(rs.getString("contact_info"));

        // Mapeo de campos de auditoría
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        if (createdAt != null) supplier.setInsertedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) supplier.setUpdatedAt(updatedAt.toLocalDateTime());

        return supplier;
    }

    @Override
    public void save(Supplier entity) {
        String sql = "INSERT INTO suppliers (name, contact_info) VALUES (?, ?)";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getContactInfo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving supplier: " + entity.getName(), e);
        }
    }

    @Override
    public void update(Supplier entity) {
        String sql = "UPDATE suppliers SET name = ?, contact_info = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getContactInfo());
            pstmt.setLong(3, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating supplier ID: " + entity.getId(), e);
        }
    }

    @Override
    public void delete(Supplier entity) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entity.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting supplier ID: " + entity.getId(), e);
        }
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers";
        List<Supplier> suppliers = new ArrayList<>();
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {
            while (rs.next()) {
                suppliers.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all suppliers", e);
        }
        return suppliers;
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return getOne(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding supplier by ID: " + id, e);
        }
    }

    @Override
    public Optional<Supplier> findByName(String name) {
        String sql = "SELECT * FROM suppliers WHERE name = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return getOne(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding supplier by name: " + name, e);
        }
    }

    @Override
    public Optional<Supplier> getOne(PreparedStatement pstmt) throws SQLException {
        try (var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM suppliers WHERE id = ?";
        try (var conn = DataBaseConfig.getInstance().getConnection();
             var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (var rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of supplier ID: " + id, e);
        }
    }
}