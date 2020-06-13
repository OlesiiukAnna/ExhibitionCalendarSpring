package ua.external.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;
import ua.external.controllers.helpers.CartStorage;
import ua.external.exceptions.InvalidDataException;
import ua.external.exceptions.exhibition.NoSuchExhibitionException;
import ua.external.exceptions.ticket.TicketsRunOutForTheDateException;
import ua.external.exceptions.user.NoSuchUserException;
import ua.external.service.interfaces.TicketService;
import ua.external.util.dto.DataForTicketOrder;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.UserDto;
import ua.external.util.enums.TicketType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ua.external.controllers.Paths.CART_DELETE_TICKET_ACTION;
import static ua.external.controllers.Paths.CART_FILE;
import static ua.external.controllers.Paths.CART_PAGE;

@Controller
public class CartController {
    private final static Logger logger = LoggerFactory.getLogger(CartController.class);

    private TicketService<TicketDto> ticketService;
    private List<DataForTicketOrder> dataForTicketOrders;

    public CartController(TicketService<TicketDto> ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(CART_PAGE)
    public String getCart(HttpServletRequest request) {
        TicketType[] ticketTypes = TicketType.values();
        request.getSession().setAttribute("ticketTypes", ticketTypes);
        return CART_FILE;
    }

    @GetMapping(CART_DELETE_TICKET_ACTION)
    public RedirectView deleteTicketFromCart(@RequestParam(value = "ticket-id-to-delete") String ticketIdToRemove,
                                             @SessionAttribute("dataForTicketsOrder") List<DataForTicketOrder> dataForTicketsOrder) {
        if (ticketIdToRemove != null) {
            CartStorage.removeFromCart(dataForTicketsOrder, Integer.parseInt(ticketIdToRemove));
        }
        return new RedirectView(CART_PAGE);
    }

    @PostMapping
    public String postCart(@SessionAttribute("dataForTicketsOrder") List<DataForTicketOrder> dataForTicketsOrder,
                           @SessionAttribute("user") UserDto user,
                           @RequestParam("cart-ticket-id") String[] ticketsIdTOSave,
                           @RequestParam("ticket-type") String[] ticketTypes,
                           Model model) {
        dataForTicketOrders = dataForTicketsOrder;

        boolean ticketsAreOrdered = false;
        if (!dataForTicketOrders.isEmpty()) {
            ticketsAreOrdered = saveTickets(user, model, ticketsIdTOSave, ticketTypes);
        }
        if (ticketsAreOrdered) {
            model.addAttribute("ticketsOrdered", true);
            dataForTicketOrders.clear();
        }
        return CART_FILE;
    }

    private boolean saveTickets(UserDto user, Model model,
                                String[] ticketsIdTOSave, String[] ticketTypes) {
        if (ticketsIdTOSave.length > 0 && ticketTypes.length > 0) {
            List<TicketDto> ticketDtos = new CopyOnWriteArrayList<>();
            for (int i = 0; i < ticketsIdTOSave.length && i < ticketTypes.length && i < dataForTicketOrders.size(); i++) {
                DataForTicketOrder data = dataForTicketOrders.get(i);
                TicketDto ticketDto = new TicketDto();
                ticketDto.setVisitDate(data.getWantedVisitDate());
                ticketDto.setTicketType(TicketType.valueOf(ticketTypes[i]));
                ticketDto.setUserId(user.getId());
                ticketDto.setExhibitionId(data.getExhibitionId());
                ticketDtos.add(ticketDto);
            }

            try {
                ticketService.saveListOfTickets(ticketDtos);
                return true;
            } catch (InvalidDataException e) {
                model.addAttribute("isInvalidData", true);
                logger.warn("Invalid input data ", e);
            } catch (TicketsRunOutForTheDateException e) {
                model.addAttribute("areTicketsRunOut", true);
                logger.warn("Tickets run out for this day ", e);
            } catch (NoSuchExhibitionException e) {
                model.addAttribute("isExhibitionNotFound", true);
                logger.warn("Such exhibition is not exists ", e);
            } catch (NoSuchUserException e) {
                model.addAttribute("isUserInNotExists", true);
                logger.warn("Such user is not exists ", e);
            }
        }
        return false;
    }
}
