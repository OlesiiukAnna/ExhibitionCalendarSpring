package ua.external.data.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.ExhibitionHallDao;
import ua.external.data.entity.ExhibitionHall;
import ua.external.exceptions.dao.DuplicateValueException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository("exhibitionHallDao")
public class ExhibitionHallDaoImpl implements ExhibitionHallDao<ExhibitionHall> {
    private Logger logger = LoggerFactory.getLogger(ExhibitionHallDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ExhibitionHall> getById(Integer id) {
        ExhibitionHall exhibitionHall = null;
        try {
            exhibitionHall = entityManager.find(ExhibitionHall.class, id);
        } catch (NoResultException e) {
            logger.error("Execution of exhibition hall with id: %d failed", id, e);
        }
        return Optional.ofNullable(exhibitionHall);
    }

    @Override
    public Optional<ExhibitionHall> getByName(String name) {
        TypedQuery<ExhibitionHall> typedQuery = entityManager
                .createQuery("select h from ExhibitionHall h where h.name=:name", ExhibitionHall.class);
        typedQuery.setParameter("name", name);
        ExhibitionHall exhibitionHall = null;
        try {
            exhibitionHall = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.error("Execution of exhibition hall with name: %s failed", name, e);
        }
        return Optional.ofNullable(exhibitionHall);
    }

    @Override
    public List<ExhibitionHall> getAll() {
        TypedQuery<ExhibitionHall> typedQuery = entityManager
                .createQuery("select h from ExhibitionHall h", ExhibitionHall.class);
        return typedQuery.getResultList();
    }

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = DuplicateValueException.class)
    @Override
    public boolean save(ExhibitionHall exhibitionHall) {
        Optional<ExhibitionHall> getExhibitionHall = getByName(exhibitionHall.getName());
        if (getExhibitionHall.isEmpty()) {
            entityManager.persist(exhibitionHall);
            return true;
        } else {
            logger.warn("Exception when save exhibition hall, duplication error");
            throw new DuplicateValueException("Error when save exhibition hall");
        }
    }

    @Override
    public Optional<ExhibitionHall> update(ExhibitionHall exhibitionHall) {
        return Optional.of(entityManager.merge(exhibitionHall));
    }

    @Override
    public boolean delete(Integer id) {
        int rowCount = entityManager
                .createQuery("delete from ExhibitionHall h where h.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        return rowCount != 0;
    }
}
