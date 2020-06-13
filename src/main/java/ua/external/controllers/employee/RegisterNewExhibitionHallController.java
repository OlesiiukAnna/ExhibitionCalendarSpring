package ua.external.controllers.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.external.exceptions.SuchExhibitionHallIsAlreadyExistsException;
import ua.external.service.interfaces.ExhibitionHallService;
import ua.external.util.dto.ExhibitionHallDto;

import static ua.external.controllers.Paths.REGISTER_NEW_EXHIBITION_HALL_FILE;
import static ua.external.controllers.Paths.REGISTER_NEW_EXHIBITION_HALL_PAGE;


@Controller
public class RegisterNewExhibitionHallController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterNewExhibitionHallController.class);

    private ExhibitionHallService<ExhibitionHallDto> exhibitionHallService;

    public RegisterNewExhibitionHallController(ExhibitionHallService<ExhibitionHallDto> exhibitionHallService) {
        this.exhibitionHallService = exhibitionHallService;
    }

    @GetMapping(REGISTER_NEW_EXHIBITION_HALL_PAGE)
    public String getRegisterNewExhibitionHall() {
        return REGISTER_NEW_EXHIBITION_HALL_FILE;
    }

    @PostMapping(REGISTER_NEW_EXHIBITION_HALL_PAGE)
    public String postRegisterNewExhibitionHall(@RequestParam("hall-name") String name,
                                                @RequestParam("hall-capacity") String capacity,
                                                Model model) {
        ExhibitionHallDto exhibitionHallDto = new ExhibitionHallDto();
        if (name == null || name.isBlank() || capacity == null || capacity.isBlank()) {
            model.addAttribute("isInvalidData", true);
            return REGISTER_NEW_EXHIBITION_HALL_FILE;
        } else {
            exhibitionHallDto.setName(name);
            exhibitionHallDto.setAllowableNumberOfVisitorsPerDay(Integer.parseInt(capacity));
        }
        boolean isSaved = false;
        try {
            isSaved = exhibitionHallService.save(exhibitionHallDto);
        } catch (SuchExhibitionHallIsAlreadyExistsException e) {
            model.addAttribute("hallNotRegistered", true);
            logger.warn("Such exhibition hall is already exists", e);
        }
        if (isSaved) {
            model.addAttribute("hallRegistered", true);
        }
        return REGISTER_NEW_EXHIBITION_HALL_FILE;
    }
}
