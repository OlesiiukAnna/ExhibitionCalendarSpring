package ua.external.data.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getById(Integer id) ;

    List<T> getAll();

    boolean save(T t);

    Optional<T> update(T t);

    boolean delete(Integer id);

}

