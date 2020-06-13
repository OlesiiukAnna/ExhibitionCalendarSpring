package ua.external.service.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.external.data.dao.interfaces.ExhibitionDao;
import ua.external.data.dao.interfaces.TicketDao;
import ua.external.data.dao.interfaces.UserDao;
import ua.external.data.entity.Exhibition;
import ua.external.data.entity.ExhibitionHall;
import ua.external.data.entity.Ticket;
import ua.external.data.entity.User;
import ua.external.exceptions.*;
import ua.external.exceptions.exhibition.NoSuchExhibitionException;
import ua.external.exceptions.ticket.NoSuchTicketException;
import ua.external.exceptions.ticket.TicketIsAlreadyPaidException;
import ua.external.exceptions.ticket.TicketsRunOutForTheDateException;
import ua.external.exceptions.user.NoSuchUserException;
import ua.external.service.interfaces.TicketService;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.mappers.DtoMapper;
import ua.external.util.enums.Role;
import ua.external.util.enums.TicketType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {
    private User user;
    private ExhibitionHall exhibitionHall;
    private Exhibition exhibition;

    private TicketDto ticketDto;
    private List<TicketDto> dtoTickets;
    private Ticket ticket;
    private Ticket ticket1;
    private List<Ticket> tickets;

    @Mock
    private TicketDao<Ticket> ticketDao;
    @Mock
    private ExhibitionDao<Exhibition> exhibitionDao;
    @Mock
    private UserDao<User> userDao;
    @Mock
    private DtoMapper<TicketDto, Ticket> ticketDtoMapper;

    private TicketService<TicketDto> testedInstance;

    @Before
    public void before() {
        testedInstance = new TicketServiceImpl(ticketDao, exhibitionDao, userDao, ticketDtoMapper);
        user = new User(1, "alex.stone@mail.com", "pass",
                "Alex", "Stone",
                "+380501234567", Role.EMPLOYEE);
        exhibitionHall = new ExhibitionHall(1, "Red", 1);
        exhibition = new Exhibition(1, "WaterFalls", "Models of different waterfalls",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 8, 31),
                100, exhibitionHall);

        ticketDto = new TicketDto();
        ticketDto.setId(1);
        ticketDto.setVisitDate(LocalDate.of(2020, 7, 15));
        ticketDto.setOrderDate(LocalDate.of(2020, 5, 20));
        ticketDto.setTicketType(TicketType.EMPLOYEE);
        ticketDto.setTicketPrice((int) (exhibition.getFullTicketPrice() * TicketType.EMPLOYEE.getPriceRate()));
        ticketDto.setPaid(false);
        ticketDto.setUserId(user.getId());
        ticketDto.setExhibitionId(exhibition.getId());
        dtoTickets = List.of(ticketDto);

        ticket = new Ticket(1, LocalDate.of(2020, 7, 15),
                LocalDate.of(2020, 5, 20), TicketType.EMPLOYEE,
                (int) (exhibition.getFullTicketPrice() * TicketType.EMPLOYEE.getPriceRate()),
                false, user, exhibition);
        tickets = List.of(ticket);
    }

    //GetById

    @Test
    public void shouldCallDaoGetByIdAndReturnTicketWhenValidId() {
        when(ticketDao.getById(1)).thenReturn(Optional.of(ticket));
        when(ticketDtoMapper.mapToDto(ticket)).thenReturn(ticketDto);
        TicketDto actual = testedInstance.getById(1).get();
        assertEquals(ticketDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByIdWhenInvalidInputId() {
        Optional<TicketDto> actual = testedInstance.getById(0);
        verify(ticketDao, never()).getById(0);
        assertEquals(Optional.empty(), actual);
    }

    //Get All Tickets By User Id

    @Test
    public void shouldCallDaoGetAllTicketsByUserIdWhenValidId() {
        when(ticketDao.getAllTicketsByUserId(1)).thenReturn(tickets);
        when(ticketDtoMapper.mapToDto(ticket)).thenReturn(ticketDto);
        List<TicketDto> actual = testedInstance.getAllTicketsByUserId(1);
        assertIterableEquals(dtoTickets, actual);
    }

    @Test
    public void shouldNotCallDaoGetAllTicketsByUserIdWhenInvalidId() {
        List<TicketDto> actual = testedInstance.getAllTicketsByUserId(0);
        verify(ticketDao, never()).getAllTicketsByUserId(0);
        assertIterableEquals(Collections.emptyList(), actual);
    }

    //GetAll

    @Test
    public void shouldReturnListOfExhibitionsWhenCallDaoGetAll() {
        when(ticketDao.getAll()).thenReturn(tickets);
        when(ticketDtoMapper.mapToDto(any())).thenReturn(ticketDto);
        List<TicketDto> actual = testedInstance.getAll();
        assertIterableEquals(dtoTickets, actual);
    }

    @Test
    public void shouldReturnEmptyListOfExhibitionHallsWhenCallDaoGetAll() {
        List<TicketDto> actual = testedInstance.getAll();
        assertIterableEquals(Collections.emptyList(), actual);
    }

    //Save

    @Test
    public void shouldReturnTrueWhenCallDaoSaveWithValidData() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        when(exhibitionDao.getById(1)).thenReturn(Optional.of(exhibition));
        when(userDao.getById(1)).thenReturn(Optional.of(user));
        when(ticketDao.save(any())).thenReturn(true);
        boolean result = testedInstance.save(ticketDto);
        verify(ticketDao).save(any());
        assertTrue(result);
    }

    // null check

    @Test(expected = InvalidDataException.class)
    public void shouldThrowExceptionWhenVisitDateIsNull() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        ticketDto.setVisitDate(null);
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test
    public void shouldReturnTrueWhenOrderDateIsNull() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        when(exhibitionDao.getById(1)).thenReturn(Optional.of(exhibition));
        when(userDao.getById(1)).thenReturn(Optional.of(user));
        when(ticketDao.save(any())).thenReturn(true);
        ticketDto.setOrderDate(null);
        boolean result = testedInstance.save(ticketDto);
        verify(ticketDao).save(any());
        assertTrue(result);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldThrowExceptionWhenTicketTypeIsNull() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        ticketDto.setTicketType(null);
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test(expected = InvalidDataException.class)
    public void shouldThrowExceptionWhenVisitorIdIsInvalid() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        ticketDto.setUserId(-1);
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test(expected = InvalidDataException.class)
    public void shouldThrowExceptionWhenExhibitionIdIsInvalid() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        ticketDto.setExhibitionId(-1);
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test(expected = NoSuchExhibitionException.class)
    public void shouldThrowExceptionWhenExhibitionNotFound() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        when(exhibitionDao.getById(ticket.getExhibition().getId())).thenReturn(Optional.empty());
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test(expected = NoSuchUserException.class)
    public void shouldThrowExceptionWhenUserNotFound() throws TicketsRunOutForTheDateException, NoSuchUserException, NoSuchExhibitionException, InvalidDataException {
        when(exhibitionDao.getById(ticket.getExhibition().getId())).thenReturn(Optional.of(exhibition));
        when(userDao.getById(ticket.getUser().getId())).thenReturn(Optional.empty());
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    @Test(expected = TicketsRunOutForTheDateException.class)
    public void shouldThrowExceptionWhenTicketsRunOutNotFound() throws NoSuchUserException, NoSuchExhibitionException, InvalidDataException, TicketsRunOutForTheDateException {
        when(exhibitionDao.getById(ticket.getExhibition().getId())).thenReturn(Optional.of(exhibition));
        when(ticketDao.countTicketsForTheDate(ticket.getVisitDate())).thenReturn(1);
        testedInstance.save(ticketDto);
        verify(ticketDao, never()).save(any());
    }

    //Update

    @Test
    public void shouldCallDaoUpdateWhenDataIsValid() throws InvalidDataException, NoSuchTicketException {
        ticketDto.setPaid(true);
        when(ticketDao.getById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketDtoMapper.mapToDto(ticket)).thenReturn(ticketDto);
        testedInstance.update(ticket.getId(), true);
        verify(ticketDao).update(any());
    }

    @Test(expected = NoSuchTicketException.class)
    public void shouldThrowExceptionWhenUpdateAndIdIsZero() throws InvalidDataException, NoSuchTicketException {
        Optional<TicketDto> actual = testedInstance.update(0, true);
        verify(ticketDao, never()).update(any());
        assertEquals(Optional.empty(), actual);
    }

    @Test(expected = NoSuchTicketException.class)
    public void shouldThrowExceptionWhenNoSuchTicketInDataBase() throws InvalidDataException, NoSuchTicketException {
        testedInstance.update(100, true);
        verify(ticketDao).update(any());
    }

    //Delete

    @Test
    public void shouldReturnTrueWhenCallDaoDeleteWithCorrectId() throws TicketIsAlreadyPaidException, NoSuchTicketException {
        when(ticketDao.getById(1)).thenReturn(Optional.of(ticket));
        when(ticketDao.delete(1)).thenReturn(true);
        testedInstance.delete(1);
        verify(ticketDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithNotValidId() throws TicketIsAlreadyPaidException, NoSuchTicketException {
        testedInstance.delete(0);
        verify(ticketDao, never()).delete(0);
    }

    @Test(expected = NoSuchTicketException.class)
    public void shouldThrowExceptionWhenCallDaoDeleteWithInvalidId() throws TicketIsAlreadyPaidException, NoSuchTicketException {
        testedInstance.delete(11);
        verify(ticketDao).delete(11);
    }

    @Test(expected = TicketIsAlreadyPaidException.class)
    public void shouldThrowExceptionWhenCallDaoDeletePaidTicket() throws TicketIsAlreadyPaidException, NoSuchTicketException {
        ticket.setPaid(true);
        when(ticketDao.getById(1)).thenReturn(Optional.of(ticket));
        testedInstance.delete(1);
        verify(ticketDao, never()).delete(1);
    }
}
