package ua.external.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.external.data.dao.impl.ExhibitionHallDaoImpl;
import ua.external.data.entity.ExhibitionHall;
import ua.external.exceptions.SuchExhibitionHallIsAlreadyExistsException;
import ua.external.exceptions.dao.DuplicateValueException;
import ua.external.util.dto.ExhibitionHallDto;
import ua.external.util.dto.mappers.DtoMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitionHallServiceImplTest {
    private ExhibitionHallDto exhibitionHallDto;
    private ExhibitionHallDto exhibitionHallDto1;
    private ExhibitionHallDto exhibitionHallDto2;
    private List<ExhibitionHallDto> exhibitionHallDtos;
    private ExhibitionHall exhibitionHall;
    private List<ExhibitionHall> exhibitionHalls;

    @Mock
    private ExhibitionHallDaoImpl exhibitionHallDao;
    @Mock
    private DtoMapper<ExhibitionHallDto, ExhibitionHall> exhibitionHallDtoMapper;

    private ExhibitionHallServiceImpl testedInstance;

    @Before
    public void before() {
        testedInstance = new ExhibitionHallServiceImpl(exhibitionHallDao, exhibitionHallDtoMapper);

        exhibitionHallDto = new ExhibitionHallDto();
        exhibitionHallDto.setId(1);
        exhibitionHallDto.setName("Red");
        exhibitionHallDto.setAllowableNumberOfVisitorsPerDay(300);

        exhibitionHallDto1 = new ExhibitionHallDto();
        exhibitionHallDto1.setId(1);
        exhibitionHallDto1.setName("Green");
        exhibitionHallDto1.setAllowableNumberOfVisitorsPerDay(200);

        exhibitionHallDto2 = new ExhibitionHallDto();
        exhibitionHallDto2.setId(1);
        exhibitionHallDto2.setName("Blue");
        exhibitionHallDto2.setAllowableNumberOfVisitorsPerDay(100);

        exhibitionHall = new ExhibitionHall(1, "Red", 300);
        exhibitionHallDtos = List.of(exhibitionHallDto, exhibitionHallDto1, exhibitionHallDto2);
        exhibitionHalls = List.of(new ExhibitionHall(1, "Red", 300),
                new ExhibitionHall(2, "Green", 200),
                new ExhibitionHall(3, "Blue", 100));
    }

    //GetById

    @Test
    public void shouldCallDaoGetByIdAndReturnExhibitionHallWhenValidId() {
        when(exhibitionHallDao.getById(1)).thenReturn(Optional.of(exhibitionHall));
        when(exhibitionHallDtoMapper.mapToDto(exhibitionHall)).thenReturn(exhibitionHallDto);
        ExhibitionHallDto actual = testedInstance.getById(1).get();
        assertEquals(exhibitionHallDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByIdWhenInvalidInputId() {
        Optional<ExhibitionHallDto> result = testedInstance.getById(0);
        verify(exhibitionHallDao, never()).getById(0);
        assertEquals(Optional.empty(), result);
    }

    //GetByName

    @Test
    public void shouldCallDaoGetByNameWhenValidInputName() {
        when(exhibitionHallDao.getByName("Red")).thenReturn(Optional.of(exhibitionHall));
        when(exhibitionHallDtoMapper.mapToDto(exhibitionHall)).thenReturn(exhibitionHallDto);
        Optional<ExhibitionHallDto> actual = testedInstance.getByName("Red");
        assertEquals(exhibitionHallDto, actual.get());
    }

    @Test
    public void shouldNotCallDaoGetByNameWhenInvalidInputName() {
        Optional<ExhibitionHallDto> result = testedInstance.getByName("");
        verify(exhibitionHallDao, never()).getByName(any());
        assertEquals(Optional.empty(), result);
    }

    //GetAll

    @Test
    public void shouldReturnListOfExhibitionHallsWhenCallDaoGetAll() {
        when(exhibitionHallDao.getAll()).thenReturn(exhibitionHalls);
        when(exhibitionHallDtoMapper.mapToDto(any())).thenReturn(exhibitionHallDto)
                .thenReturn(exhibitionHallDto1).thenReturn(exhibitionHallDto2);
        List<ExhibitionHallDto> actual = testedInstance.getAll();
        assertIterableEquals(exhibitionHallDtos, actual);
    }

    @Test
    public void shouldReturnEmptyListOfExhibitionHallsWhenCallDaoGetAll() {
        when(exhibitionHallDao.getAll()).thenReturn(Collections.emptyList());
        List<ExhibitionHallDto> actual = testedInstance.getAll();
        assertIterableEquals(Collections.emptyList(), actual);
    }

    //Save

    @Test
    public void shouldReturnTrueWhenCallDaoSaveWithValidData() throws SuchExhibitionHallIsAlreadyExistsException {
        when(exhibitionHallDao.save(any())).thenReturn(true);
        boolean result = testedInstance.save(exhibitionHallDto);
        verify(exhibitionHallDao).save(any());
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoSaveWithInvalidName() throws SuchExhibitionHallIsAlreadyExistsException {
        exhibitionHallDto.setName("");
        boolean result = testedInstance.save(exhibitionHallDto);
        verify(exhibitionHallDao, never()).save(any());
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoSaveWithInvalidNumberOfVisitorsPerDay() throws SuchExhibitionHallIsAlreadyExistsException {
        exhibitionHallDto.setAllowableNumberOfVisitorsPerDay(-1);
        boolean result = testedInstance.save(exhibitionHallDto);
        verify(exhibitionHallDao, never()).save(any());
        assertFalse(result);
    }

    @Test(expected = SuchExhibitionHallIsAlreadyExistsException.class)
    public void shouldThrowExceptionWhenCallDaoSaveWithValidDataTwice() throws SuchExhibitionHallIsAlreadyExistsException {
        when(exhibitionHallDao.save(any())).thenReturn(true).thenThrow(DuplicateValueException.class);
        testedInstance.save(exhibitionHallDto);
        testedInstance.save(exhibitionHallDto);
    }

    //Update

    @Test
    public void shouldCallDaoUpdateWhenValidData() {
        when(exhibitionHallDao.getById(exhibitionHallDto.getId())).thenReturn(Optional.of(exhibitionHall));
        when(exhibitionHallDtoMapper.mapFromDto(exhibitionHallDto)).thenReturn(exhibitionHall);
        testedInstance.update(exhibitionHallDto);
        verify(exhibitionHallDao).update(any());
    }

    @Test
    public void shouldNotCallDaoUpdateWhenInvalidId() {
        exhibitionHallDto.setId(0);
        Optional<ExhibitionHallDto> result = testedInstance
                .update(exhibitionHallDto);

        verify(exhibitionHallDao, never()).update(any());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void shouldNotCallDaoUpdateWhenInvalidName() {
        exhibitionHallDto.setName("");
        Optional<ExhibitionHallDto> result = testedInstance
                .update(exhibitionHallDto);
        verify(exhibitionHallDao, never()).update(any());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void shouldNotCallDaoUpdateWhenInvalidNumberOfVisitorsPerDay() {
        exhibitionHallDto.setAllowableNumberOfVisitorsPerDay(-1);
        Optional<ExhibitionHallDto> result = testedInstance
                .update(exhibitionHallDto);
        verify(exhibitionHallDao, never()).update(any());
        assertEquals(Optional.empty(), result);
    }

    //Delete

    @Test
    public void shouldReturnTrueWhenCallDaoDeleteWithCorrectId() {
        when(exhibitionHallDao.delete(1)).thenReturn(true);
        testedInstance.delete(1);
        verify(exhibitionHallDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithNonExistingId() {
        when(exhibitionHallDao.delete(1)).thenReturn(false);
        testedInstance.delete(1);
        verify(exhibitionHallDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithInvalidId() {
        testedInstance.delete(0);
        verify(exhibitionHallDao, never()).delete(0);
    }

}