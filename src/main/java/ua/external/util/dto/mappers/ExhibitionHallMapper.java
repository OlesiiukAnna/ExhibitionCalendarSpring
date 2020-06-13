package ua.external.util.dto.mappers;

import ua.external.data.entity.ExhibitionHall;
import ua.external.util.dto.ExhibitionHallDto;

public class ExhibitionHallMapper implements DtoMapper<ExhibitionHallDto, ExhibitionHall> {

    @Override
    public ExhibitionHallDto mapToDto(ExhibitionHall exhibitionHall) {
        ExhibitionHallDto dto = new ExhibitionHallDto();
        dto.setId(exhibitionHall.getId());
        dto.setName(exhibitionHall.getName());
        dto.setAllowableNumberOfVisitorsPerDay(exhibitionHall.getAllowableNumberOfVisitorsPerDay());
        return dto;
    }

    @Override
    public ExhibitionHall mapFromDto(ExhibitionHallDto exhibitionHallDto) {
        ExhibitionHall exhibitionHall = new ExhibitionHall();
//        exhibitionHall.setId(exhibitionHallDto.getId());
        exhibitionHall.setName(exhibitionHallDto.getName());
        exhibitionHall.setAllowableNumberOfVisitorsPerDay(exhibitionHallDto.getAllowableNumberOfVisitorsPerDay());
        return exhibitionHall;
    }
}
