package ua.external.data.dao.interfaces;

import ua.external.data.dao.Dao;
import ua.external.data.entity.ExhibitionHall;

import java.util.Optional;

public interface ExhibitionHallDao<T extends ExhibitionHall> extends Dao<T> {

    Optional<T> getByName(String name);

}