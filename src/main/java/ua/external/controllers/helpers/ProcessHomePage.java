package ua.external.controllers.helpers;

import org.springframework.ui.Model;
import ua.external.service.interfaces.ExhibitionService;
import ua.external.service.interfaces.TicketService;
import ua.external.util.dto.ExhibitionDto;
import ua.external.util.dto.TicketDto;
import ua.external.util.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessHomePage {

    public static void processPage(Model model,
                                   UserDto user,
                                   TicketService<TicketDto> ticketService,
                                   ExhibitionService<ExhibitionDto> exhibitionService) {
        List<TicketDto> tickets = ticketService.getAllTicketsByUserId(user.getId());
        tickets.sort((ticket1, ticket2) -> Boolean.compare(ticket1.isPaid(), ticket2.isPaid()));

        model.addAttribute("myTickets", tickets);

        Map<Integer, ExhibitionDto> exhibitionDtos = new ConcurrentHashMap<>();
        for (TicketDto ticketDto : tickets) {
            exhibitionDtos.put(ticketDto.getExhibitionId(),
                    exhibitionService.getById(ticketDto.getExhibitionId()).get());
        }
        model.addAttribute("ticketExhibitions", exhibitionDtos);
    }
}
