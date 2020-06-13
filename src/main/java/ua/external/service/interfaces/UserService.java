package ua.external.service.interfaces;

import ua.external.exceptions.user.SuchUserIsAlreadyExistsException;
import ua.external.exceptions.user.InvalidUserException;
import ua.external.service.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface UserService<T> extends Service<T> {

    Optional<T> getByEmail(String email);

    boolean save(T t) throws SuchUserIsAlreadyExistsException, InvalidUserException, NoSuchAlgorithmException;

    Optional<T> update(T t) throws InvalidUserException, NoSuchAlgorithmException;

    boolean isPasswordCorrectForUser(String incomePassword, String userPassword);

    boolean delete(int id);
}
