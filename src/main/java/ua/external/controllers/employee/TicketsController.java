package ua.external.controllers.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.ticket.NoSuchTicketException;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.service.interfaces.TicketService;
import ua.external.service.interfaces.UserService;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static ua.external.controllers.Paths.CONFIRM_TICKETS_FILE;
import static ua.external.controllers.Paths.CONFIRM_TICKETS_PAGE;

@Controller
public class TicketsController {
    private final static Logger logger = LoggerFactory.getLogger(TicketsController.class);

    private TicketService<TicketDto> ticketService;
    private UserService<UserDto> userService;
    private ExhibitionService<ExhibitionDto> exhibitionService;

    public TicketsController(TicketService<TicketDto> ticketService,
                             UserService<UserDto> userService,
                             ExhibitionService<ExhibitionDto> exhibitionService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping(CONFIRM_TICKETS_PAGE)
    public String getAllTickets(Model model) {
        getTicketDtos(model);
        return CONFIRM_TICKETS_FILE;
    }

    @PostMapping(CONFIRM_TICKETS_PAGE)
    public RedirectView postAllTickets(@RequestParam("ticketIdsToConfirm") String[] ticketIdsToConfirmIncome,
                                       Model model) {
        List<TicketDto> tickets = getTicketDtos(model);
        int[] ticketIdsToConfirm = List.of(ticketIdsToConfirmIncome)
                .stream()
                .mapToInt(Integer::parseInt)
                .toArray();

        if (ticketIdsToConfirm.length == 1) {
            try {
                ticketService.update(ticketIdsToConfirm[0], true);
            } catch (InvalidDataException e) {
                logger.warn("Invalid input data", e);
            } catch (NoSuchTicketException e) {
                logger.warn("There is no such ticket", e);
            }
        } else {
            List<TicketDto> ticketsToUpdate = new CopyOnWriteArrayList<>();
            for (TicketDto ticketDto : tickets) {
                for (int ticketId : ticketIdsToConfirm) {
                    if (ticketDto.getId() == ticketId) {
                        ticketDto.setPaid(true);
                        ticketsToUpdate.add(ticketDto);
                    }
                }
            }
            try {
                ticketService.updateListOfTickets(ticketsToUpdate);
            } catch (NoSuchTicketException e) {
                logger.warn("There is no such ticket", e);
            }
        }
        return new RedirectView(CONFIRM_TICKETS_PAGE);
    }

    private List<TicketDto> getTicketDtos(Model model) {
        List<TicketDto> tickets = ticketService.getAll();
        tickets.sort((ticket1, ticket2) -> Boolean.compare(ticket1.isPaid(), ticket2.isPaid()));
        model.addAttribute("tickets", tickets);

        Map<Integer, UserDto> userDtos = new ConcurrentHashMap<>();
        for (TicketDto ticketDto : tickets) {
            userDtos.put(ticketDto.getUserId(), userService.getById(ticketDto.getUserId()).get());
        }
        model.addAttribute("ticketOwners", userDtos);

        Map<Integer, ExhibitionDto> exhibitionDtos = new ConcurrentHashMap<>();
        for (TicketDto ticketDto : tickets) {
            exhibitionDtos.put(ticketDto.getExhibitionId(),
                    exhibitionService.getById(ticketDto.getExhibitionId()).get());
        }
        model.addAttribute("ticketExhibitions", exhibitionDtos);
        return tickets;
    }
}
