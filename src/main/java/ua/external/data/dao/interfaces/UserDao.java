package ua.external.data.dao.interfaces;

import ua.external.data.dao.Dao;
import ua.external.data.entity.User;

import java.util.Optional;

public interface UserDao<T extends User> extends Dao<T> {

    Optional<T> getByEmail(String email);

}
