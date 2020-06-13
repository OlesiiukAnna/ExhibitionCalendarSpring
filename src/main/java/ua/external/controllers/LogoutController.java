package ua.external.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.view.RedirectView;
import ua.external.util.dto.DataForTicketOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ua.external.controllers.Paths.INDEX_PAGE;
import static ua.external.controllers.Paths.LOGOUT_PAGE;

@Controller
public class LogoutController {

    @GetMapping(LOGOUT_PAGE)
    public RedirectView postLogout(HttpServletRequest request,
                                   @SessionAttribute("dataForTicketsOrder") List<DataForTicketOrder> dataForTicketsOrder,
                                   SessionStatus sessionStatus) {
        dataForTicketsOrder.clear();
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("password");
        request.getSession().removeAttribute("role");
        request.getSession().removeAttribute("dataForTicketsOrder");
        request.getSession().setAttribute("userInSystem", false);
        request.getSession().invalidate();
        sessionStatus.setComplete();
        return new RedirectView(INDEX_PAGE);
    }
}
