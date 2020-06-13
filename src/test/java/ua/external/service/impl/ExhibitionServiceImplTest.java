package ua.external.service.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.external.data.dao.impl.ExhibitionDaoImpl;
import ua.external.data.dao.impl.ExhibitionHallDaoImpl;
import ua.external.data.entity.Exhibition;
import ua.external.data.entity.ExhibitionHall;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.InvalidDateException;
import ua.external.exceptions.exhibition.PriceBelowZeroException;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.mappers.DtoMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitionServiceImplTest {
    private ExhibitionDto exhibitionDto;
    private ExhibitionDto exhibitionDto1;
    private List<ExhibitionDto> exhibitionDtos;
    private Exhibition exhibition;
    private Exhibition exhibition1;
    private ExhibitionHall exhibitionHall;
    private List<Exhibition> exhibitions;

    @Mock
    private ExhibitionDaoImpl exhibitionDao;
    @Mock
    private ExhibitionHallDaoImpl exhibitionHallDao;
    @Mock
    private DtoMapper<ExhibitionDto, Exhibition> exhibitionDtoMapper;

    private ExhibitionServiceImpl testedInstance;

    @Before
    public void before() {
        testedInstance = new ExhibitionServiceImpl(exhibitionDao, exhibitionHallDao, exhibitionDtoMapper);
        exhibitionHall = new ExhibitionHall(1, "Red", 300);

        exhibitionDto = new ExhibitionDto();
        exhibitionDto.setId(1);
        exhibitionDto.setName("WaterFalls");
        exhibitionDto.setDescription("Models of different waterfalls");
        exhibitionDto.setBeginDate(LocalDate.of(2020, 6, 1));
        exhibitionDto.setEndDate(LocalDate.of(2020, 8, 31));
        exhibitionDto.setFullTicketPrice(100);
        exhibitionDto.setExhibitionHallId(exhibitionHall.getId());

        exhibitionDto1 = new ExhibitionDto();
        exhibitionDto1.setId(1);
        exhibitionDto1.setName("Flowers");
        exhibitionDto1.setDescription("Flowers of spring");
        exhibitionDto1.setBeginDate(LocalDate.of(2020, 3, 1));
        exhibitionDto1.setEndDate(LocalDate.of(2020, 5, 31));
        exhibitionDto1.setFullTicketPrice(100);
        exhibitionDto1.setExhibitionHallId(exhibitionHall.getId());
        exhibitionDtos = List.of(exhibitionDto, exhibitionDto1);

        exhibition = new Exhibition(1, "WaterFalls", "Models of different waterfalls",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 8, 31),
                100, exhibitionHall);
        exhibition1 = new Exhibition(2, "Flowers", "Flowers of spring",
                LocalDate.of(2020, 3, 1),
                LocalDate.of(2020, 5, 31),
                100, exhibitionHall);
        exhibitions = List.of(exhibition, exhibition1);
    }

    //GetById

    @Test
    public void shouldCallDaoGetByIdAndReturnExhibitionWhenValidId() {
        when(exhibitionDao.getById(1)).thenReturn(Optional.of(exhibition));
        when(exhibitionDtoMapper.mapToDto(any())).thenReturn(exhibitionDto);
        ExhibitionDto actual = testedInstance.getById(1).get();
        assertEquals(exhibitionDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByIdWhenInvalidInputId() {
        Optional<ExhibitionDto> actual = testedInstance.getById(0);
        verify(exhibitionDao, never()).getById(0);
        assertEquals(Optional.empty(), actual);
    }

    //GetByName

    @Test
    public void shouldCallDaoGetByNameWhenValidInputName() {
        when(exhibitionDao.getByName("WaterFalls")).thenReturn(Optional.of(exhibition));
        when(exhibitionDtoMapper.mapToDto(any())).thenReturn(exhibitionDto);
        ExhibitionDto actual = testedInstance.getByName("WaterFalls").get();
        assertEquals(exhibitionDto, actual);
    }

    @Test
    public void shouldNotCallDaoGetByNameWhenInvalidInputName() {
        Optional<ExhibitionDto> actual = testedInstance.getByName("");
        verify(exhibitionDao, never()).getByName(any());
        assertEquals(Optional.empty(), actual);
    }

    //GetAllByExhibitionHallId

    @Test
    public void shouldCallDaoGetByExhibitionHallIdWhenValidInputName() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        when(exhibitionDtoMapper.mapToDto(any())).thenReturn(exhibitionDto)
                .thenReturn(exhibitionDto1);
        List<ExhibitionDto> actual = testedInstance.getAllByExhibitionHallId(1);
        assertIterableEquals(exhibitionDtos, actual);
    }

    @Test
    public void shouldNotCallDaoGetByExhibitionHallIdWhenInvalidId() {
        List<ExhibitionDto> actual = testedInstance.getAllByExhibitionHallId(0);
        verify(exhibitionDao, never()).getAllByExhibitionHallId(0);
        assertIterableEquals(Collections.emptyList(), actual);
    }


    //GetAll

    @Test
    public void shouldReturnListOfExhibitionsWhenCallDaoGetAll() {
        when(exhibitionDao.getAll()).thenReturn(exhibitions);
        when(exhibitionDtoMapper.mapToDto(any())).thenReturn(exhibitionDto)
                .thenReturn(exhibitionDto1);
        List<ExhibitionDto> actual = testedInstance.getAll();
        assertIterableEquals(exhibitionDtos, actual);
    }

    @Test
    public void shouldReturnEmptyListOfExhibitionHallsWhenCallDaoGetAll() {
        List<ExhibitionDto> actual = testedInstance.getAll();
        assertIterableEquals(Collections.emptyList(), actual);
    }

    //Save

    @Test
    public void shouldReturnTrueWhenCallDaoSaveWithValidData() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionHallDao.getById(1)).thenReturn(Optional.of(exhibitionHall));
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(List.of(exhibition1));
        when(exhibitionDtoMapper.mapFromDto(exhibitionDto)).thenReturn(exhibition);
        when(exhibitionDao.save(any())).thenReturn(true);
        boolean result = testedInstance.save(exhibitionDto);
        verify(exhibitionDao).save(any());
        assertTrue(result);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldReturnThrowExceptionWhenCallDaoSaveWithEmptyHall() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionHallDao.getById(1)).thenReturn(Optional.empty());
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(List.of(exhibition1));
        boolean result = testedInstance.save(exhibitionDto);
        verify(exhibitionDao).save(any());
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenSaveWithNameIsNull() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        exhibitionDto.setName(null);
        boolean result = testedInstance.save(exhibitionDto);
        verify(exhibitionDao, never()).save(any());
        assertFalse(result);
    }

    @Test(expected = InvalidDateException.class)
    public void shouldThrowExceptionWhenSaveWithDatesAreOnAnotherExhibitionDates() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionDao.getAllByExhibitionHallId(exhibition.getExhibitionHall().getId()))
                .thenReturn(exhibitions);
        testedInstance.save(exhibitionDto);
        verify(exhibitionHallDao, never()).save(any());
    }

    @Test(expected = InvalidDateException.class)
    public void shouldThrowExceptionWhenSaveWithDatesCoincideWithAnotherExhibitionInHall() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionDao.getAllByExhibitionHallId(exhibition.getExhibitionHall().getId()))
                .thenReturn(exhibitions);
        exhibitionDto.setBeginDate(exhibition.getBeginDate().plusDays(2));
        exhibitionDto.setEndDate(exhibition.getEndDate().minusDays(2));
        testedInstance.save(exhibitionDto);
        verify(exhibitionHallDao, never()).save(any());
    }

    @Test(expected = PriceBelowZeroException.class)
    public void shouldReturnFalseWhenSaveWithFullTicketPriceBelowZero() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        exhibitionDto.setFullTicketPrice(-1);
        boolean result = testedInstance.save(exhibitionDto);
        verify(exhibitionHallDao, never()).save(any());
        assertFalse(result);
    }

    @Test(expected = InvalidDateException.class)
    public void shouldThrowExceptionWhenCallDaoSaveWithSameValidDataTwice() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        testedInstance.save(exhibitionDto);
        testedInstance.save(exhibitionDto);
    }

    //Update

    @Test
    public void shouldCallDaoUpdateWhenValidData() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        when(exhibitionHallDao.getById(1)).thenReturn(Optional.of(exhibitionHall));
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(List.of(exhibition1));
        when(exhibitionDtoMapper.mapFromDto(exhibitionDto)).thenReturn(exhibition);
        testedInstance.update(exhibitionDto);
        verify(exhibitionDao).update(any());
    }

    @Test
    public void shouldNotCallDaoUpdateWhenIdIsInvalid() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        exhibitionDto.setId(0);
        testedInstance.update(exhibitionDto);
        verify(exhibitionDao, never()).update(any());
    }

    @Test
    public void shouldNotCallDaoUpdateWhenNameIsNull() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        exhibitionDto.setName(null);
        testedInstance.update(exhibitionDto);
        verify(exhibitionDao, never()).update(any());
    }

    @Test
    public void shouldNotCallDaoUpdateWhenDescriptionIsNull() throws PriceBelowZeroException, InvalidDateException, InvalidDataException {
        exhibitionDto.setDescription(null);
        testedInstance.update(exhibitionDto);
        verify(exhibitionDao, never()).update(any());
    }

    //Delete

    @Test
    public void shouldReturnTrueWhenCallDaoDeleteWithCorrectId() {
        when(exhibitionDao.delete(1)).thenReturn(true);
        testedInstance.delete(1);
        verify(exhibitionDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithNonExistingId() {
        when(exhibitionDao.delete(1)).thenReturn(false);
        testedInstance.delete(1);
        verify(exhibitionDao).delete(1);
    }

    @Test
    public void shouldReturnFalseWhenCallDaoDeleteWithInvalidId() {
        testedInstance.delete(0);
        verify(exhibitionDao, never()).delete(0);
    }

}
