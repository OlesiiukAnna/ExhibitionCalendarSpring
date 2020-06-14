package ua.external.data.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.ExhibitionDao;
import ua.external.data.entity.Exhibition;
import ua.external.exceptions.dao.DuplicateValueException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository("exhibitionDao")
@Transactional
public class ExhibitionDaoImpl implements ExhibitionDao<Exhibition> {
    private Logger logger = LoggerFactory.getLogger(ExhibitionDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Exhibition> getById(Integer id) {
        Exhibition exhibition = null;
        try {
            exhibition = entityManager.find(Exhibition.class, id);
        } catch (NoResultException e) {
            logger.error("Execution of exhibition with id: %d failed", id, e);
        }
        return Optional.ofNullable(exhibition);
    }

    @Override
    public Optional<Exhibition> getByName(String name) {
        TypedQuery<Exhibition> typedQuery = entityManager
                .createQuery("select e from Exhibition e where e.name=:name", Exhibition.class);
        typedQuery.setParameter("name", name);
        Exhibition exhibition = null;
        try {
            exhibition = typedQuery.getSingleResult();
        } catch (NoResultException e) {
            logger.error("Execution of exhibition with name: %s failed", name, e);
        }
        return Optional.ofNullable(exhibition);
    }

    @Override
    public List<Exhibition> getAllByExhibitionHallId(Integer id) {
        TypedQuery<Exhibition> typedQuery = entityManager
                .createQuery("select e from Exhibition e where e.exhibitionHall.id=:exhibition_hall_id", Exhibition.class);
        typedQuery.setParameter("exhibition_hall_id", id);
        return typedQuery.getResultList();
    }

    @Override
    public List<Exhibition> getAll() {
        TypedQuery<Exhibition> typedQuery = entityManager
                .createQuery("select e from Exhibition e", Exhibition.class);
        return typedQuery.getResultList();
    }

    @Transactional(propagation = Propagation.MANDATORY, noRollbackFor = DuplicateValueException.class)
    @Override
    public boolean save(Exhibition exhibition) {
        Optional<Exhibition> getExhibition = Optional.empty();
        if (exhibition.getId() != null && exhibition.getId() != 0) {
            getExhibition = getById(exhibition.getId());
        }
        if (getExhibition.isEmpty()) {
            entityManager.persist(exhibition);
            return true;
        } else {
            logger.warn("Exception when save exhibition hall, duplication error");
            throw new DuplicateValueException("Error when save exhibition hall");
        }
    }

    @Override
    public Optional<Exhibition> update(Exhibition exhibition) {
        return Optional.of(entityManager.merge(exhibition));
    }

    @Override
    public boolean delete(Integer id) {
        int rowCount = entityManager
                .createQuery("delete from Exhibition e where e.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        return rowCount != 0;
    }

}
