package ua.external.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import ua.external.controllers.helpers.CartStorage;
import ua.external.exceptions.InvalidDataException;
import ua.external.service.interfaces.ExhibitionHallService;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.util.dto.DataForTicketOrder;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.ExhibitionHallDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ua.external.controllers.Paths.EXHIBITION_HALL_FILE;
import static ua.external.controllers.Paths.EXHIBITION_HALL_PAGE;

@Controller
public class ExhibitionHallController {
    private static final Logger logger = LoggerFactory.getLogger(ExhibitionHallController.class);

    private ExhibitionHallService<ExhibitionHallDto> exhibitionHallService;
    private ExhibitionService<ExhibitionDto> exhibitionService;

    public ExhibitionHallController(ExhibitionHallService<ExhibitionHallDto> exhibitionHallService,
                                    ExhibitionService<ExhibitionDto> exhibitionService) {
        this.exhibitionHallService = exhibitionHallService;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping(EXHIBITION_HALL_PAGE)
    public String getExhibitionHall(@RequestParam(value = "exhibition-hall-id") String incomeId, Model model) {
        if (incomeId != null && !incomeId.isBlank()) {
            int id = Integer.parseInt(incomeId);
            Optional<ExhibitionHallDto> exhibitionHallDto = exhibitionHallService.getById(id);
            if (exhibitionHallDto.isPresent()) {
                model.addAttribute("exhibitionHall", exhibitionHallDto.get());
                List<ExhibitionDto> exhibitions = exhibitionService.getAllByExhibitionHallId(id);
                model.addAttribute("exhibitions", exhibitions);
            }
        }
        return EXHIBITION_HALL_FILE;
    }

    @PostMapping(EXHIBITION_HALL_PAGE)
    public String postExhibitionHall(Model model,
                                     @RequestParam(value = "exhibition-hall-id") String incomeId,
                                     @SessionAttribute("dataForTicketsOrder") List<DataForTicketOrder> dataForTicketsOrder,
                                     @RequestParam Map<String, String> allParams) {
        if (incomeId != null && !incomeId.isBlank()) {
            int id = Integer.parseInt(incomeId);
            List<ExhibitionDto> exhibitions = exhibitionService.getAllByExhibitionHallId(id);

            try {
                CartStorage.addToCart(model, exhibitions, dataForTicketsOrder, allParams);
            } catch (InvalidDataException e) {
                logger.info("incorrect input data ", e);
            }
        }
        return EXHIBITION_HALL_FILE;
    }
}
