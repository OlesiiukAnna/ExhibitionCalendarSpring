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
import ua.external.service.interfaces.ExhibitionService;
import ua.external.util.dto.DataForTicketOrder;
import ua.external.util.dto.ExhibitionDto;

import java.util.List;
import java.util.Map;

import static ua.external.controllers.Paths.EXHIBITIONS_FILE;
import static ua.external.controllers.Paths.EXHIBITIONS_PAGE;

@Controller
public class ExhibitionsController {
    private static final Logger logger = LoggerFactory.getLogger(ExhibitionsController.class);

    private ExhibitionService<ExhibitionDto> exhibitionService;

    public ExhibitionsController(ExhibitionService<ExhibitionDto> exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @GetMapping(EXHIBITIONS_PAGE)
    public String getExhibitions(Model model) {
        List<ExhibitionDto> exhibitions = exhibitionService.getAll();
        model.addAttribute("exhibitions", exhibitions);
        return EXHIBITIONS_FILE;
    }

    @PostMapping(EXHIBITIONS_PAGE)
    public String postExhibitions(Model model,
                                  @SessionAttribute("dataForTicketsOrder") List<DataForTicketOrder> dataForTicketsOrder,
                                  @RequestParam Map<String, String> allParams) {
        List<ExhibitionDto> exhibitions = exhibitionService.getAll();
        try {
            CartStorage.addToCart(model, exhibitions, dataForTicketsOrder, allParams);
        } catch (InvalidDataException e) {
            logger.info("incorrect input data ", e);
        }
        return EXHIBITIONS_FILE;
    }
}
