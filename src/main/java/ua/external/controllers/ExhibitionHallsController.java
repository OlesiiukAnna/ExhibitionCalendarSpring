package ua.external.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.external.service.interfaces.ExhibitionHallService;
import ua.external.util.dto.ExhibitionHallDto;

import java.util.List;

import static ua.external.controllers.Paths.EXHIBITION_HALLS_FILE;
import static ua.external.controllers.Paths.EXHIBITION_HALLS_PAGE;

@Controller
public class ExhibitionHallsController {
    private ExhibitionHallService<ExhibitionHallDto> exhibitionHallService;

    public ExhibitionHallsController(ExhibitionHallService<ExhibitionHallDto> exhibitionHallService) {
        this.exhibitionHallService = exhibitionHallService;
    }

    @GetMapping(EXHIBITION_HALLS_PAGE)
    public String getExhibitionHalls(Model model) {
        List<ExhibitionHallDto> exhibitionHallDtos = exhibitionHallService.getAll();
        model.addAttribute("exhibitionHalls", exhibitionHallDtos);
        return EXHIBITION_HALLS_FILE;
    }
}
