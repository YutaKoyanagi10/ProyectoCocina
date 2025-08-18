package org.proyectococinav2.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    void save(T entity);
    void update(T entity);
    List<T> findAll();

}