package ua.external.service.interfaces;

import ua.external.exceptions.SuchExhibitionHallIsAlreadyExistsException;
import ua.external.service.Service;

import java.util.Optional;

public interface ExhibitionHallService<T> extends Service<T> {

    Optional<T> getByName(String name);

    boolean save(T t) throws SuchExhibitionHallIsAlreadyExistsException;

    Optional<T> update(T t);

    boolean delete(int id);
}
