package ua.external.controllers.employee;

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

import static ua.external.controllers.Paths.EMPLOYEE_HOME_PAGE_FILE;
import static ua.external.controllers.Paths.EMPLOYEE_HOME_PAGE_PAGE;

@Controller
public class EmployeeHomePageController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeHomePageController.class);

    private TicketService<TicketDto> ticketService;
    private ExhibitionService<ExhibitionDto> exhibitionService;

    public EmployeeHomePageController(TicketService<TicketDto> ticketService,
                                      ExhibitionService<ExhibitionDto> exhibitionService) {
        this.ticketService = ticketService;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping(EMPLOYEE_HOME_PAGE_PAGE)
    public String getEmployeeHomePage(@SessionAttribute("user") UserDto user,
                                      Model model) {
        ProcessHomePage.processPage(model, user, ticketService, exhibitionService);
        return EMPLOYEE_HOME_PAGE_FILE;
    }

    @PostMapping(EMPLOYEE_HOME_PAGE_PAGE)
    public RedirectView postEmployeeHomePage(@RequestParam("ticket-to-delete") String ticketToDelete,
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
        return new RedirectView(EMPLOYEE_HOME_PAGE_PAGE);
    }
}
