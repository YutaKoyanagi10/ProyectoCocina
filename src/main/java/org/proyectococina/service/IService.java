package org.proyectococina.service;

import java.util.List;
import java.util.Optional;

public interface IService<D, ID> {
    void save(D dto);
    void delete(D dto);
    List<D> findAll();
    Optional<D> findById(ID id);
    boolean existsById(ID id);
    List<String> findAllNames();
}
