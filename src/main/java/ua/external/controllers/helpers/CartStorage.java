package ua.external.controllers.helpers;

import org.springframework.ui.Model;
import ua.external.exceptions.InvalidDataException;
import ua.external.util.dto.DataForTicketOrder;
import ua.external.util.dto.ExhibitionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CartStorage {
    private static List<DataForTicketOrder> dataForTicketOrders;
    private static int idGenerator;

    public static void addToCart(Model model,
                                 List<ExhibitionDto> exhibitions,
                                 List<DataForTicketOrder> dataForTicketsOrder,
                                 Map<String, String> allParams) throws InvalidDataException {
        model.addAttribute("exhibitions", exhibitions);

        String exhibitionId = allParams.get("exhibition-id");
        String exhibitionName = allParams.get("exhibition-name");
        String exhibitionHallId = allParams.get("exhibition-hall-id");
        String wantedVisitDate = allParams.get("visit-date");
        String ticketsQuantity = allParams.get("wanted-tickets-quantity");
        String fullTicketPrice = allParams.get("full-ticket-price");

        if (exhibitionId == null || exhibitionName == null || exhibitionName.isBlank() ||
                exhibitionHallId == null || wantedVisitDate == null || wantedVisitDate.isBlank() ||
                ticketsQuantity == null || fullTicketPrice == null) {
            throw new InvalidDataException();
        }

        dataForTicketOrders = dataForTicketsOrder;
        for (int i = 0; i < Integer.parseInt(ticketsQuantity); i++) {
            DataForTicketOrder dataForTicketOrder = new DataForTicketOrder();
            dataForTicketOrder.setIdInCart(idGenerator);
            dataForTicketOrder.setExhibitionId(Integer.parseInt(exhibitionId));
            dataForTicketOrder.setExhibitionName(exhibitionName);
            dataForTicketOrder.setExhibitionHallId(Integer.parseInt(exhibitionHallId));
            dataForTicketOrder.setWantedVisitDate(LocalDate.parse(wantedVisitDate));
            dataForTicketOrder.setTicketPrice(Integer.parseInt(fullTicketPrice));
            dataForTicketOrders.add(dataForTicketOrder);
            idGenerator++;
        }
    }

    public static void removeFromCart(List<DataForTicketOrder> dataForTicketsOrder, int id) {
        dataForTicketOrders = dataForTicketsOrder;
        if (dataForTicketOrders != null) {
            dataForTicketOrders.removeIf(data -> data.getIdInCart() == id);
        }

    }
}
