package org.proyectococinav2.repository.impl;

import org.proyectococinav2.config.DataBaseConfig;
import org.proyectococinav2.domain.model.Supplier;
import org.proyectococinav2.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierRepositoryImpl implements SupplierRepository {

    @Override
    public Optional<Supplier> findSupplierByName(String name) {
        String sql = "SELECT * FROM SUPPLIER WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setName(resultSet.getString("NAME"));
                return Optional.of(supplier);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding supplier by name: " + name, e);
        }
    }

    @Override
    public Optional<Long> findSupplierIdByName(String name) {
        String sql = "SELECT ID FROM SUPPLIER WHERE NAME = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("ID"));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding supplier ID by name: " + name, e);
        }
    }

    @Override
    public void save(Supplier entity) {
        String sql = "INSERT INTO SUPPLIER (NAME, CONTACT_INFO) values (?, ?)";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getContactInfo());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error saving supplier: " + entity, e);
        }
    }

    @Override
    public void update(Supplier entity) {
        String sql = "UPDATE SUPPLIER SET NAME = ?, CONTACT_INFO = ? WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getContactInfo());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error updating supplier: " + entity, e);
        }
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM SUPPLIER";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {
            List<Supplier> suppliers = new ArrayList<>();
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setContactInfo(resultSet.getString("CONTACT_INFO"));
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all suppliers", e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM SUPPLIER WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting supplier by ID: " + aLong, e);
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM SUPPLIER WHERE ID = ?";
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
            throw new RuntimeException("Error checking if supplier exists by ID: " + aLong, e);
        }
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        String sql = "SELECT * FROM SUPPLIER WHERE ID = ?";
        try (var connection = DataBaseConfig.getInstance().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(resultSet.getLong("ID"));
                supplier.setName(resultSet.getString("NAME"));
                supplier.setContactInfo(resultSet.getString("CONTACT_INFO"));
                return Optional.of(supplier);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding supplier by ID: " + id, e);
        }
    }
}
