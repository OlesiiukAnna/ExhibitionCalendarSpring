package ua.external.service.interfaces;

import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.InvalidDateException;
import ua.external.exceptions.exhibition.PriceBelowZeroException;
import ua.external.service.Service;

import java.util.List;
import java.util.Optional;

public interface ExhibitionService<T> extends Service<T> {

    Optional<T> getByName(String name);

    List<T> getAllByExhibitionHallId(int id);

    boolean save(T t) throws InvalidDateException, PriceBelowZeroException, InvalidDataException;

    Optional<T> update(T t) throws InvalidDateException, PriceBelowZeroException, InvalidDataException;

    boolean delete(int id);
}
