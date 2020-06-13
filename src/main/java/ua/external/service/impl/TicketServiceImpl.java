package ua.external.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.ExhibitionDao;
import ua.external.data.dao.interfaces.TicketDao;
import ua.external.data.dao.interfaces.UserDao;
import ua.external.data.entity.Exhibition;
import ua.external.data.entity.Ticket;
import ua.external.data.entity.User;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.NoSuchExhibitionException;
import ua.external.exceptions.ticket.NoSuchTicketException;
import ua.external.exceptions.user.NoSuchUserException;
import ua.external.exceptions.ticket.TicketIsAlreadyPaidException;
import ua.external.exceptions.ticket.TicketsRunOutForTheDateException;
import ua.external.service.interfaces.TicketService;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.mappers.DtoMapper;
import ua.external.util.enums.TicketType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static ua.external.util.enums.TicketType.EMPLOYEE;
import static ua.external.util.enums.TicketType.KID;
import static ua.external.util.enums.TicketType.PENSIONER;
import static ua.external.util.enums.TicketType.STUDENT;

@Transactional
public class TicketServiceImpl implements TicketService<TicketDto> {
    private Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private TicketDao<Ticket> ticketDao;
    private ExhibitionDao<Exhibition> exhibitionDao;
    private UserDao<User> userDao;

    private DtoMapper<TicketDto, Ticket> dtoMapper;

    public TicketServiceImpl(TicketDao<Ticket> ticketDao, ExhibitionDao<Exhibition> exhibitionDao,
                             UserDao<User> userDao, DtoMapper<TicketDto, Ticket> ticketDtoMapper) {
        this.ticketDao = ticketDao;
        this.exhibitionDao = exhibitionDao;
        this.userDao = userDao;
        this.dtoMapper = ticketDtoMapper;
    }

    @Override
    public Optional<TicketDto> getById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        TicketDto ticketDto = null;
        if (ticketDao.getById(id).isPresent()) {
            ticketDto = dtoMapper.mapToDto(ticketDao.getById(id).get());
        }
        return Optional.ofNullable(ticketDto);
    }

    @Override
    public List<TicketDto> getAllTicketsByUserId(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        return ticketDao.getAllTicketsByUserId(userId).stream()
                .map(ticket -> dtoMapper.mapToDto(ticket))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getAll() {
        return ticketDao.getAll().stream()
                .map(ticket -> dtoMapper.mapToDto(ticket))
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(TicketDto ticketDto) throws InvalidDataException, TicketsRunOutForTheDateException, NoSuchExhibitionException, NoSuchUserException {
        Ticket ticket = getAndCheckTicketFromTicketDto(ticketDto);
        return ticketDao.save(ticket);
    }

    private Ticket getAndCheckTicketFromTicketDto(TicketDto ticketDto) throws InvalidDataException, NoSuchExhibitionException, TicketsRunOutForTheDateException, NoSuchUserException {
        if (ticketDto.getVisitDate() == null || ticketDto.getTicketType() == null ||
                ticketDto.getUserId() <= 0 || ticketDto.getExhibitionId() <= 0 ||
                ticketDto.getVisitDate().isBefore(LocalDate.now())) {
            logger.error("Invalid input data ");
            throw new InvalidDataException();
        }
        LocalDate visitDate = ticketDto.getVisitDate();
        LocalDate orderDate = ticketDto.getOrderDate();
        if (orderDate == null) {
            orderDate = LocalDate.now();
        }

        Optional<Exhibition> demandedExhibition = exhibitionDao.getById(ticketDto.getExhibitionId());
        if (demandedExhibition.isEmpty()) {
            throw new NoSuchExhibitionException();
        }

        Exhibition targetExhibition = demandedExhibition.get();
        int visitorsCapacity = targetExhibition.getExhibitionHall().getAllowableNumberOfVisitorsPerDay();
        int countTicketsForTheDate = ticketDao.countTicketsForTheDate(visitDate);
        if (countTicketsForTheDate >= visitorsCapacity) {
            throw new TicketsRunOutForTheDateException();
        }

        Optional<User> visitor = userDao.getById(ticketDto.getUserId());
        if (visitor.isEmpty()) {
            throw new NoSuchUserException();
        }

        TicketType type = ticketDto.getTicketType();
        int ticketPrice = calculateTicketPrice(targetExhibition.getFullTicketPrice(), type);
        return new Ticket(visitDate, orderDate, type,
                ticketPrice, visitor.get(), targetExhibition);
    }

    @Override
    public boolean saveListOfTickets(List<TicketDto> tickets) throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        List<Ticket> ticketsToSave = new CopyOnWriteArrayList<>();
        for (TicketDto ticketDto : tickets) {
            ticketsToSave.add(getAndCheckTicketFromTicketDto(ticketDto));
        }

        return ticketDao.saveListOfTickets(ticketsToSave);
    }

    @Override
    public int calculateTicketPrice(int fullPrice, TicketType type) {
        int price = fullPrice;
        switch (type) {
            case FULL:
                price = fullPrice;
                break;
            case STUDENT:
                price = (int) (fullPrice * STUDENT.getPriceRate());
                break;
            case KID:
                price = (int) (fullPrice * KID.getPriceRate());
                break;
            case PENSIONER:
                price = (int) (fullPrice * PENSIONER.getPriceRate());
                break;
            case EMPLOYEE:
                price = (int) (fullPrice * EMPLOYEE.getPriceRate());
                break;
        }
        return price;
    }

    @Override
    public Optional<TicketDto> update(int id, boolean isPaid) throws NoSuchTicketException {
        Ticket ticket = getTicketFroUpdate(id, isPaid);
        ticketDao.update(ticket);
        return Optional.of(dtoMapper.mapToDto(ticket));
    }

    private Ticket getTicketFroUpdate(int id, boolean isPaid) throws NoSuchTicketException {
        if (id <= 0) {
            throw new NoSuchTicketException();
        }
        Optional<Ticket> ticketToUpdate = ticketDao.getById(id);
        if (ticketToUpdate.isEmpty()) {
            throw new NoSuchTicketException();
        }
        Ticket ticket = ticketToUpdate.get();
        ticket.setPaid(isPaid);
        return ticket;
    }

    @Override
    public boolean updateListOfTickets(List<TicketDto> ticketDtos) throws NoSuchTicketException {
        List<Ticket> tickets = new CopyOnWriteArrayList<>();
        for (TicketDto ticketDto : ticketDtos) {
            tickets.add(getTicketFroUpdate(ticketDto.getId(), ticketDto.isPaid()));
        }
        return ticketDao.updateListOfTickets(tickets);
    }

    @Override
    public boolean delete(Integer id) throws TicketIsAlreadyPaidException, NoSuchTicketException {
        if (id <= 0) {
            return false;
        }

        Optional<Ticket> ticket = ticketDao.getById(id);

        if (ticket.isPresent()) {
            if (ticket.get().isPaid()) {
                throw new TicketIsAlreadyPaidException();
            }
            return ticketDao.delete(id);
        }
        throw new NoSuchTicketException();
    }
}
