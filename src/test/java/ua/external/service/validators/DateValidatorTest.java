package ua.external.service.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.external.data.dao.impl.ExhibitionDaoImpl;
import ua.external.data.entity.Exhibition;
import ua.external.data.entity.ExhibitionHall;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DateValidatorTest {
    private LocalDate beginDate;
    private LocalDate endDate;
    private Exhibition exhibition1;
    private Exhibition exhibition2;
    private ExhibitionHall exhibitionHall;
    private List<Exhibition> exhibitions;

    @Mock
    private ExhibitionDaoImpl exhibitionDao;

    @Before
    public void before() {
        beginDate = LocalDate.of(2020, 1, 1);
        endDate = LocalDate.of(2020, 1, 31);
        exhibitionHall = new ExhibitionHall(1, "Red", 300);
        exhibition1 = new Exhibition(1, "Flowers", "Flowers of spring",
                LocalDate.of(2020, 3, 1),
                LocalDate.of(2020, 5, 31),
                100, exhibitionHall);
        exhibition2 = new Exhibition(2, "WaterFalls", "Models of different waterfalls",
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 8, 31),
                100, exhibitionHall);
        exhibitions = List.of(exhibition1, exhibition2);
    }

    @Test
    public void shouldReturnFalseWhenDatesAreFree() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertFalse(actual);
    }

    @Test
    public void shouldReturnTrueWhenBeginDateTheSameAsSomeExhibitionBeginDate() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        beginDate = exhibitions.get(0).getBeginDate();
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnTrueWhenAndDateTheSameAsSomeExhibitionBeginDate() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        endDate = exhibitions.get(0).getEndDate();
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnTrueWhenBeginDateBetweenExhibitionDates() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        beginDate = exhibitions.get(0).getBeginDate().plusDays(6);
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnTrueWhenEndDateBetweenExhibitionDates() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        endDate = exhibitions.get(0).getEndDate().minusDays(6);
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnTrueWhenDatesCoverExhibitionDates() {
        when(exhibitionDao.getAllByExhibitionHallId(1)).thenReturn(exhibitions);
        beginDate = exhibitions.get(0).getBeginDate().minusDays(1);
        endDate = exhibitions.get(1).getEndDate().plusDays(1);
        boolean actual = DateValidator.isDatesBusy(exhibitionHall.getId(),
                beginDate, endDate, exhibitionDao);
        assertTrue(actual);
    }
}
