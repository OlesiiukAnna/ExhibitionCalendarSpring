package ua.external.data.dao.interfaces;

import ua.external.data.dao.Dao;
import ua.external.data.entity.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketDao<T extends Ticket> extends Dao<T> {

    List<Ticket> getAllTicketsByUserId(Integer id);

    boolean saveListOfTickets(List<Ticket> tickets);

    Integer countTicketsForTheDate(LocalDate date);

    boolean updateListOfTickets(List<Ticket> tickets);
}
