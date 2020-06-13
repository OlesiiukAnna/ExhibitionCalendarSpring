package ua.external.service.interfaces;

import ua.external.exceptions.*;
import ua.external.exceptions.exhibition.NoSuchExhibitionException;
import ua.external.exceptions.ticket.NoSuchTicketException;
import ua.external.exceptions.ticket.TicketIsAlreadyPaidException;
import ua.external.exceptions.ticket.TicketsRunOutForTheDateException;
import ua.external.exceptions.user.NoSuchUserException;
import ua.external.service.Service;
import ua.external.util.enums.TicketType;

import java.util.List;
import java.util.Optional;

public interface TicketService<T> extends Service<T> {

    List<T> getAllTicketsByUserId(int userId);

    boolean save(T t)
            throws InvalidDataException, TicketsRunOutForTheDateException,
            NoSuchExhibitionException, NoSuchUserException;

    boolean saveListOfTickets(List<T> ticketDtos) throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException;

    int calculateTicketPrice(int fullPrice, TicketType type);

    Optional<T> update(int id, boolean isPaid) throws InvalidDataException, NoSuchTicketException;

    boolean updateListOfTickets(List<T> ts) throws NoSuchTicketException;

    boolean delete(Integer id) throws TicketIsAlreadyPaidException, NoSuchTicketException;
}
