package ua.external.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.external.data.dao.interfaces.UserDao;
import ua.external.data.entity.User;
import ua.external.exceptions.user.SuchUserIsAlreadyExistsException;
import ua.external.exceptions.dao.DuplicateValueException;
import ua.external.exceptions.user.InvalidUserException;
import ua.external.service.interfaces.UserService;
import ua.external.util.PasswordEncryptor;
import ua.external.util.dto.UserDto;
import ua.external.util.dto.mappers.DtoMapper;
import ua.external.util.enums.Role;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private UserDto userDto;
    private UserDto userDto1;
    private List<UserDto> userDtos;
    private User user;
    private User user1;
    private List<User> users;

    @Mock
    private UserDao<User> userDao;
    @Mock
    private DtoMapper<UserDto, User> dtoMapper;

    private UserService<UserDto> testedInstance;

    @Before
    public void before() {
        testedInstance = new UserServiceImpl(userDao, dtoMapper);

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("alex.stone@mail.com");
        userDto.setPassword("pass");
        userDto.setFirstName("Alex");
        userDto.setLastName("Stone");
        userDto.setPhone("+380501234567");
        userDto.setRole(Role.EMPLOYEE);

        userDto1 = new UserDto();
        userDto1.setId(2);
        userDto1.setEmail("ben-winters@mail.com");
        userDto1.setPassword("pass");
        userDto1.setFirstName("Ben");
        userDto1.setLastName("Winters");
        userDto1.setPhone("+380501234567");
        userDto1.setRole(Role.VISITOR);

        userDtos = List.of(userDto, userDto1);

        user = new User(1, "alex.stone@mail.com", "pass",
                "Alex", "Stone",
                "+380501234567", Role.EMPLOYEE);
        user1 = new User(2, "ben-winters@mail.com", "pass",
                "Ben", "Winters",
                "+380501234567", Role.VISITOR);
        users = List.of(user, user1);
    }

    //GetById

    @Test
    public void shouldCallDaoGetByIdAndReturnExhibitionWhenValidId() {
        when(userDao.getById(1)).thenReturn(Optional.of(user));
        when(dtoMapper.mapToDto(user)).thenReturn(userDto);
        UserDto actual = testedInstance.getById(1).get();
        assertEquals(userDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByIdWhenInvalidInputId() {
        Optional<UserDto> actual = testedInstance.getById(0);
        verify(userDao, never()).getById(0);
        assertEquals(Optional.empty(), actual);
    }

    //GetByEmail

    @Test
    public void shouldCallDaoGetByEmailWhenValidInputEmail() {
        when(userDao.getByEmail("alex.stone@mail.com")).thenReturn(Optional.of(user));
        when(dtoMapper.mapToDto(user)).thenReturn(userDto);
        UserDto actual = testedInstance.getByEmail("alex.stone@mail.com").get();
        assertEquals(userDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByEmailWhenInvalidInputEmail() {
        Optional<UserDto> actual = testedInstance.getByEmail("");
        verify(userDao, never()).getByEmail(any());
        assertEquals(Optional.empty(), actual);
    }

    //GetAll

    @Test
    public void shouldReturnListOfExhibitionsWhenCallDaoGetAll() {
        when(userDao.getAll()).thenReturn(users);
        when(dtoMapper.mapToDto(any())).thenReturn(userDto)
                .thenReturn(userDto1);
        List<UserDto> actual = testedInstance.getAll();
        assertIterableEquals(userDtos, actual);
    }

    @Test
    public void shouldReturnEmptyListOfExhibitionHallsWhenCallDaoGetAll() {
        List<UserDto> actual = testedInstance.getAll();
        assertIterableEquals(Collections.emptyList(), actual);
    }

    //Save

    @Test
    public void shouldReturnTrueWhenCallDaoSaveWithValidData() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        when(userDao.save(any())).thenReturn(true);
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        boolean result = testedInstance.save(userDto);
        verify(userDao).save(any());
        assertTrue(result);
    }

    // null check

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputPasswordIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setPassword(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputFirstNameIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setFirstName(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputLastNameIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setLastName(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputPhoneIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setPhone(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputEmailIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setEmail(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputRoleIsNull() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setRole(null);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    // validation check

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputFirstNameIsInvalid() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setFirstName(" ");
        user.setFirstName(" ");
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputLastNameIsInvalid() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setLastName("");
        user.setLastName("");
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputPhoneIsInvalid() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setPhone("12");
        user.setPhone("12");
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowExceptionWhenInputEmailIsInvalid() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        userDto.setEmail("email");
        user.setEmail("email");
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        testedInstance.save(userDto);
        verify(userDao, never()).save(any());
    }

    @Test(expected = SuchUserIsAlreadyExistsException.class)
    public void shouldThrowExceptionWhenCallDaoSaveWithUserThatAlreadyExists() throws SuchUserIsAlreadyExistsException, NoSuchAlgorithmException, InvalidUserException {
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        when(userDao.save(any())).thenReturn(true).thenThrow(DuplicateValueException.class);
        testedInstance.save(userDto);
        testedInstance.save(userDto);
    }

    //Update

    @Test
    public void shouldCallDaoUpdateWhenValidData() throws NoSuchAlgorithmException, InvalidUserException {
        userDto.setPassword(PasswordEncryptor.encrypt(user.getPassword()));
        user.setPassword(PasswordEncryptor.encrypt(user.getPassword()));
        when(userDao.getById(user.getId())).thenReturn(Optional.of(new User(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole())));
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        testedInstance.update(userDto);
        verify(userDao).update(any());
    }

    @Test
    public void shouldNotCallDaoUpdateWhenPasswordsNotEquals() throws NoSuchAlgorithmException, InvalidUserException {
        when(userDao.getById(user.getId())).thenReturn(Optional.of(new User(
                user.getEmail(),
                PasswordEncryptor.encrypt(user.getPassword()),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole())));
        userDto.setPassword("password");
        when(dtoMapper.mapFromDto(userDto)).thenReturn(user);
        Optional<UserDto> actual = testedInstance.update(userDto);
        verify(userDao, never()).update(any());
        assertEquals(Optional.empty(), actual);
    }

    //Delete

    @Test
    public void shouldReturnTrueWhenCallDaoDeleteWithCorrectId() {
        when(userDao.delete(1)).thenReturn(true);
        testedInstance.delete(1);
        verify(userDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithNonExistingId() {
        when(userDao.delete(1)).thenReturn(false);
        testedInstance.delete(1);
        verify(userDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithInvalidId() {
        testedInstance.delete(0);
        verify(userDao, never()).delete(0);
    }

}
