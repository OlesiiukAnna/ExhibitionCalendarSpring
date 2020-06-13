package ua.external.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.external.data.dao.interfaces.ExhibitionHallDao;
import ua.external.data.entity.ExhibitionHall;
import ua.external.exceptions.SuchExhibitionHallIsAlreadyExistsException;
import ua.external.exceptions.dao.DuplicateValueException;
import ua.external.service.interfaces.ExhibitionHallService;
import ua.external.util.dto.ExhibitionHallDto;
import ua.external.util.dto.mappers.DtoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class ExhibitionHallServiceImpl implements ExhibitionHallService<ExhibitionHallDto> {
    private Logger logger = LoggerFactory.getLogger(ExhibitionHallServiceImpl.class);

    private ExhibitionHallDao<ExhibitionHall> exhibitionHallDao;

    private DtoMapper<ExhibitionHallDto, ExhibitionHall> dtoMapper;

    public ExhibitionHallServiceImpl(ExhibitionHallDao<ExhibitionHall> exhibitionHallDao,
                                     DtoMapper<ExhibitionHallDto, ExhibitionHall> exhibitionHallDtoMapper) {
        this.exhibitionHallDao = exhibitionHallDao;
        this.dtoMapper = exhibitionHallDtoMapper;
    }

    @Override
    public Optional<ExhibitionHallDto> getById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        ExhibitionHallDto exhibitionHallDto = null;
        if (exhibitionHallDao.getById(id).isPresent()) {
            exhibitionHallDto = dtoMapper.mapToDto(exhibitionHallDao.getById(id).get());
        }
        return Optional.ofNullable(exhibitionHallDto);
    }

    @Override
    public Optional<ExhibitionHallDto> getByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        ExhibitionHallDto exhibitionHallDto = null;
        if (exhibitionHallDao.getByName(name).isPresent()) {
            exhibitionHallDto = dtoMapper.mapToDto(exhibitionHallDao.getByName(name).get());
        }
        return Optional.ofNullable(exhibitionHallDto);
    }

    @Override
    public List<ExhibitionHallDto> getAll() {
        return exhibitionHallDao.getAll().stream()
                .map(exhibitionHall -> dtoMapper.mapToDto(exhibitionHall))
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(ExhibitionHallDto exhibitionHallDto) throws SuchExhibitionHallIsAlreadyExistsException {
        if (exhibitionHallDto.getName() == null || exhibitionHallDto.getName().isBlank() ||
                exhibitionHallDto.getAllowableNumberOfVisitorsPerDay() < 0) {
            return false;
        }
        ExhibitionHall exhibitionHall = dtoMapper.mapFromDto(exhibitionHallDto);

        try {
            return exhibitionHallDao.save(exhibitionHall);
        } catch (DuplicateValueException e) {
            logger.error(e.getMessage());
            throw new SuchExhibitionHallIsAlreadyExistsException();
        }
    }

    @Override
    public Optional<ExhibitionHallDto> update(ExhibitionHallDto exhibitionHallDto) {
        if (exhibitionHallDto.getId() <= 0 || exhibitionHallDto.getName() == null ||
                exhibitionHallDto.getName().isBlank() ||
                exhibitionHallDto.getAllowableNumberOfVisitorsPerDay() < 0) {
            return Optional.empty();
        }
        ExhibitionHall exhibitionHall = dtoMapper.mapFromDto(exhibitionHallDto);
        Optional<ExhibitionHall> exhibitionHallToUpdate = exhibitionHallDao.getById(exhibitionHall.getId());
        ExhibitionHallDto exhibitionHallDtoToReturn = null;
        if (exhibitionHallToUpdate.isPresent() &&
                exhibitionHallDao.update(exhibitionHall).isPresent()) {
            exhibitionHallDtoToReturn = dtoMapper.mapToDto(exhibitionHallDao.update(exhibitionHall).get());
        }
        return Optional.ofNullable(exhibitionHallDtoToReturn);
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }
        return exhibitionHallDao.delete(id);
    }
}
