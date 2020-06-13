package ua.external.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.ExhibitionDao;
import ua.external.data.dao.interfaces.ExhibitionHallDao;
import ua.external.data.entity.Exhibition;
import ua.external.data.entity.ExhibitionHall;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.InvalidDateException;
import ua.external.exceptions.exhibition.PriceBelowZeroException;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.service.validators.DateValidator;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.mappers.DtoMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class ExhibitionServiceImpl implements ExhibitionService<ExhibitionDto> {
    private Logger logger = LoggerFactory.getLogger(ExhibitionServiceImpl.class);

    private ExhibitionDao<Exhibition> exhibitionDao;
    private ExhibitionHallDao<ExhibitionHall> exhibitionHallDao;

    private DtoMapper<ExhibitionDto, Exhibition> dtoMapper;

    public ExhibitionServiceImpl(ExhibitionDao<Exhibition> exhibitionDao,
                                 ExhibitionHallDao<ExhibitionHall> exhibitionHallDao,
                                 DtoMapper<ExhibitionDto, Exhibition> exhibitionDtoMapper) {
        this.exhibitionDao = exhibitionDao;
        this.exhibitionHallDao = exhibitionHallDao;
        this.dtoMapper = exhibitionDtoMapper;
    }

    @Override
    public Optional<ExhibitionDto> getById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        ExhibitionDto exhibitionDto = null;
        if (exhibitionDao.getById(id).isPresent()) {
            exhibitionDto = dtoMapper.mapToDto(exhibitionDao.getById(id).get());
        }
        return Optional.ofNullable(exhibitionDto);
    }

    @Override
    public Optional<ExhibitionDto> getByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        ExhibitionDto exhibitionDto = null;
        if (exhibitionDao.getByName(name).isPresent()) {
            exhibitionDto = dtoMapper.mapToDto(exhibitionDao.getByName(name).get());
        }
        return Optional.ofNullable(exhibitionDto);
    }

    @Override
    public List<ExhibitionDto> getAllByExhibitionHallId(int exhibitionHallId) {
        if (exhibitionHallId <= 0) {
            return Collections.emptyList();
        }
        return exhibitionDao.getAllByExhibitionHallId(exhibitionHallId).stream()
                .map(exhibition -> dtoMapper.mapToDto(exhibition))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExhibitionDto> getAll() {
        return exhibitionDao.getAll().stream()
                .map(exhibition -> dtoMapper.mapToDto(exhibition))
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(ExhibitionDto exhibitionDto) throws InvalidDateException, PriceBelowZeroException, InvalidDataException {
        if (exhibitionDto.getName() == null || exhibitionDto.getDescription() == null) {
            return false;
        }

        Exhibition exhibition = createExhibitionFromInputData(exhibitionDto);
        return exhibitionDao.save(exhibition);
    }

    private Exhibition createExhibitionFromInputData(ExhibitionDto exhibitionDto)
            throws InvalidDateException, PriceBelowZeroException, InvalidDataException {
        if (exhibitionDto.getFullTicketPrice() < 0) {
            throw new PriceBelowZeroException();
        }

        LocalDate beginDate = exhibitionDto.getBeginDate();
        LocalDate endDate = exhibitionDto.getEndDate();
        if (beginDate.isAfter(endDate) ||
                DateValidator.isDatesBusy(exhibitionDto.getExhibitionHallId(), beginDate, endDate, exhibitionDao)) {
            logger.warn("Dates are not free or begin date after end date ");
            throw new InvalidDateException();
        }

        ExhibitionHall exhibitionHall;
        if (exhibitionHallDao.getById(exhibitionDto.getExhibitionHallId()).isEmpty()) {
            logger.error("Exhibition hall with such id doesn't exists ");
            throw new InvalidDataException();
        } else {
            exhibitionHall = exhibitionHallDao
                    .getById(exhibitionDto.getExhibitionHallId())
                    .get();
        }

        Exhibition exhibition = dtoMapper.mapFromDto(exhibitionDto);
        exhibition.setExhibitionHall(exhibitionHall);
        return exhibition;
    }

    @Override
    public Optional<ExhibitionDto> update(ExhibitionDto exhibitionDto) throws InvalidDateException, PriceBelowZeroException, InvalidDataException {
        if (exhibitionDto.getId() <= 0 || exhibitionDto.getName() == null || exhibitionDto.getDescription() == null) {
            return Optional.empty();
        }
        Exhibition exhibition = createExhibitionFromInputData(exhibitionDto);
        ExhibitionDto exhibitionDtoToReturn = null;
        if (exhibitionDao.update(exhibition).isPresent()) {
            exhibitionDtoToReturn = dtoMapper.mapToDto(exhibitionDao.update(exhibition).get());
        }
        return Optional.ofNullable(exhibitionDtoToReturn);
    }


    @Override
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }
        return exhibitionDao.delete(id);
    }
}
