package ua.external.controllers.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;
import ua.external.controllers.helpers.ProcessHomePage;
import ua.external.exceptions.ticket.NoSuchTicketException;
import ua.external.exceptions.ticket.TicketIsAlreadyPaidException;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.service.interfaces.TicketService;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.UserDto;

import static ua.external.controllers.Paths.VISITOR_HOME_PAGE_FILE;
import static ua.external.controllers.Paths.VISITOR_HOME_PAGE_PAGE;

@Controller
public class VisitorHomePageController {
    private static final Logger logger = LoggerFactory.getLogger(VisitorHomePageController.class);

    private TicketService<TicketDto> ticketService;
    private ExhibitionService<ExhibitionDto> exhibitionService;


    public VisitorHomePageController(TicketService<TicketDto> ticketService,
                                     ExhibitionService<ExhibitionDto> exhibitionService) {
        this.ticketService = ticketService;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping(VISITOR_HOME_PAGE_PAGE)
    public String getVisitorHomePage(@SessionAttribute("user") UserDto user,
                                     Model model) {
        ProcessHomePage.processPage(model, user, ticketService, exhibitionService);
        return VISITOR_HOME_PAGE_FILE;
    }

    @PostMapping(VISITOR_HOME_PAGE_PAGE)
    public RedirectView postVisitorHomePage(@RequestParam("ticket-to-delete") String ticketToDelete,
                                            @SessionAttribute("user") UserDto user,
                                            Model model) {
        ProcessHomePage.processPage(model, user, ticketService, exhibitionService);
        if (ticketToDelete != null) {
            try {
                Integer ticketId = Integer.parseInt(ticketToDelete);
                ticketService.delete(ticketId);
            } catch (TicketIsAlreadyPaidException e) {
                logger.warn("Tickets are already paid", e);
            } catch (NoSuchTicketException e) {
                logger.warn("There is no such ticket", e);
            }
        }
        return new RedirectView(VISITOR_HOME_PAGE_PAGE);
    }
}
