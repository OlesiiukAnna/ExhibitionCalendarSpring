package ua.external.data.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.UserDao;
import ua.external.data.entity.User;
import ua.external.exceptions.dao.DuplicateValueException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao<User> {
    private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getById(Integer id) {
        User user = null;
        try {
            user = entityManager.find(User.class, id);
        } catch (NoResultException e) {
            logger.error("Execution of user with id: %d failed", id, e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        TypedQuery<User> typedQuery = entityManager
                .createQuery("select u from User u where u.email=:email", User.class);
        typedQuery.setParameter("email", email);
        User user = null;
        try {
            user = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.error("Execution of user with email: %s failed", email, e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> typedQuery = entityManager
                .createQuery("select u from User u", User.class);
        return typedQuery.getResultList();
    }

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = DuplicateValueException.class)
    @Override
    public boolean save(User user) {
        Optional<User> getUser = getByEmail(user.getEmail());
        if (getUser.isEmpty()) {
            entityManager.persist(user);
            return true;
        } else {
            logger.warn("Exception when save user, duplication error");
            throw new DuplicateValueException("Error when save user");
        }
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.of(entityManager.merge(user));
    }

    @Override
    public boolean delete(Integer id) {
        int rowCount = entityManager
                .createQuery("delete from User u where u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        return rowCount != 0;
    }
}
