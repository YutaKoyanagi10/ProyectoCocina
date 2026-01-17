package org.proyectococina.repository;

import java.util.List;
import java.util.Optional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

interface IRepository<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    Optional<T> findByName(String name);
    T mapResultSetToEntity(ResultSet rs) throws Exception;
    Optional<T> getOne(PreparedStatement pstmt) throws SQLException;
    boolean existsById(ID id);
}