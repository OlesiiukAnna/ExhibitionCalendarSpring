package ua.external.data.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.TicketDao;
import ua.external.data.entity.Ticket;
import ua.external.exceptions.dao.DuplicateValueException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("ticketDao")
//@Transactional(propagation = Propagation.MANDATORY)
public class TicketDaoImpl implements TicketDao<Ticket> {
    private Logger logger = LoggerFactory.getLogger(TicketDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ticket> getById(Integer id) {
        Ticket ticket = null;
        try {
            ticket = entityManager.find(Ticket.class, id);
        } catch (NoResultException e) {
            logger.error("Execution of ticket with id: %d failed", id, e);
        }
        return Optional.ofNullable(ticket);
    }

    @Override
    public List<Ticket> getAllTicketsByUserId(Integer id) {
        TypedQuery<Ticket> typedQuery = entityManager
                .createQuery("select t from Ticket t where t.user.id=:user_id", Ticket.class);
        typedQuery.setParameter("user_id", id);
        return typedQuery.getResultList();
    }

    @Override
    public List<Ticket> getAll() {
        TypedQuery<Ticket> typedQuery = entityManager
                .createQuery("select t From Ticket t", Ticket.class);
        return typedQuery.getResultList();
    }

    @Override
    public Integer countTicketsForTheDate(LocalDate date) {
        TypedQuery<Long> typedQuery = entityManager
                .createQuery("select count(t.id) from Ticket t where t.visitDate=:date", Long.class)
                .setParameter("date", date);
        return Math.toIntExact(typedQuery.getSingleResult());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public boolean save(Ticket ticket) {
        Optional<Ticket> getTicket = Optional.empty();
        if (ticket.getId() != null && ticket.getId() != 0) {
            getTicket = getById(ticket.getId());
        }
        if (getTicket.isEmpty()) {
            entityManager.persist(ticket);
            return true;
        } else {
            logger.warn("Exception when save ticket, duplication error");
            throw new DuplicateValueException("Error when save ticket");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    @Override
    public boolean saveListOfTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            save(ticket);
        }
        return true;
    }

    @Override
    public Optional<Ticket> update(Ticket ticket) {
        return Optional.of(entityManager.merge(ticket));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
    @Override
    public boolean updateListOfTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            entityManager.merge(ticket);
        }
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        int rowCount = entityManager
                .createQuery("delete from Ticket t where t.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        return rowCount != 0;
    }

//    public void setEntityManager(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
}
