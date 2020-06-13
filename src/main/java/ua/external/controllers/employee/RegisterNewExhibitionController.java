package ua.external.controllers.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.InvalidDateException;
import ua.external.exceptions.exhibition.PriceBelowZeroException;
import ua.external.service.interfaces.ExhibitionHallService;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.ExhibitionHallDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ua.external.controllers.Paths.REGISTER_NEW_EXHIBITION_FILE;
import static ua.external.controllers.Paths.REGISTER_NEW_EXHIBITION_PAGE;

@Controller
public class RegisterNewExhibitionController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterNewExhibitionController.class);

    private ExhibitionService<ExhibitionDto> exhibitionService;
    private ExhibitionHallService<ExhibitionHallDto> exhibitionHallService;

    public RegisterNewExhibitionController(ExhibitionService<ExhibitionDto> exhibitionService,
                                           ExhibitionHallService<ExhibitionHallDto> exhibitionHallService) {
        this.exhibitionService = exhibitionService;
        this.exhibitionHallService = exhibitionHallService;
    }

    @GetMapping(REGISTER_NEW_EXHIBITION_PAGE)
    public String getRegisterNewExhibitionForm(Model model) {
        List<ExhibitionHallDto> exhibitionHallDtos = exhibitionHallService.getAll();
        model.addAttribute("exhibitionHalls", exhibitionHallDtos);
        return REGISTER_NEW_EXHIBITION_FILE;
    }

    @PostMapping(REGISTER_NEW_EXHIBITION_PAGE)
    public String postRegisterNewExhibitionForm(@RequestParam Map<String, String> allParams,
                                                Model model) {
        ExhibitionDto exhibitionDto = new ExhibitionDto();
        String name = allParams.get("exhibition-name");
        String description = allParams.get("exhibition-description");
        String beginDate = allParams.get("exhibition-begin-date");
        String endDate = allParams.get("exhibition-end-date");
        String fullTicketPrice = allParams.get("exhibition-full-ticket-price");
        String exhibitionHallId = allParams.get("exhibition-hall-id");

        if (name == null || description == null || fullTicketPrice == null ||
                name.isBlank() || description.isBlank() || fullTicketPrice.isBlank()) {
            model.addAttribute("exhibitionRegistered", false);
            model.addAttribute("isInvalidData", true);
            return REGISTER_NEW_EXHIBITION_FILE;
        } else {
            exhibitionDto.setName(name);
            exhibitionDto.setDescription(description);
            exhibitionDto.setBeginDate(LocalDate.parse(beginDate));
            exhibitionDto.setEndDate(LocalDate.parse(endDate));
            exhibitionDto.setFullTicketPrice(Integer.parseInt(fullTicketPrice));
            exhibitionDto.setExhibitionHallId(Integer.parseInt(exhibitionHallId));
        }

        boolean isSaved = false;
        try {
            isSaved = exhibitionService.save(exhibitionDto);
        } catch (InvalidDateException e) {
            model.addAttribute("isDatesWrong", true);
            logger.warn("Invalid input dates", e);
        } catch (PriceBelowZeroException e) {
            logger.warn("Wrong price", e);
        } catch (InvalidDataException e) {
            model.addAttribute("isInvalidData", true);
            logger.warn("Invalid input data", e);
        }
        if (isSaved) {
            model.addAttribute("exhibitionRegistered", true);
        }
        return REGISTER_NEW_EXHIBITION_FILE;
    }
}
