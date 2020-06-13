package ua.external.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ua.external.service.interfaces.UserService;
import ua.external.util.PasswordEncryptor;
import ua.external.util.dto.DataForTicketOrder;
import ua.external.util.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.nonNull;
import static ua.external.controllers.Paths.INDEX_PAGE;
import static ua.external.controllers.Paths.LOGIN_FILE;
import static ua.external.controllers.Paths.LOGIN_PAGE;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private List<DataForTicketOrder> dataForTicketOrders;
    private UserService<UserDto> userService;

    public LoginController(UserService<UserDto> userService) {
        this.userService = userService;
        this.dataForTicketOrders = new CopyOnWriteArrayList<>();
    }

    @GetMapping(LOGIN_PAGE)
    public String getLogin() {
        return LOGIN_FILE;
    }

    @PostMapping(LOGIN_PAGE)
    public RedirectView postLogin(HttpServletRequest request, Model model) {
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        final HttpSession session = request.getSession();

        if (nonNull(session) &&
                nonNull(session.getAttribute("email")) &&
                nonNull(session.getAttribute("password"))) {

            return new RedirectView(INDEX_PAGE);
        }

        Optional<UserDto> userOptional = userService.getByEmail(email);
        UserDto userDto = new UserDto();
        boolean isAccessGranted = false;
        if (userOptional.isPresent()) {
            String inputPass = null;
            try {
                inputPass = PasswordEncryptor.encrypt(password);
            } catch (NoSuchAlgorithmException e) {
                logger.error("Missed algorithm ", e);
            }
            isAccessGranted = userService.isPasswordCorrectForUser(inputPass, userOptional.get().getPassword());
            userDto = userOptional.get();
        }
        if (isAccessGranted) {
            request.getSession().setAttribute("userInSystem", true);
            request.getSession().setAttribute("user", userDto);
            request.getSession().setAttribute("email", userDto.getEmail());
            request.getSession().setAttribute("password", userDto.getPassword());
            request.getSession().setAttribute("role", userDto.getRole());
            request.getSession().setAttribute("dataForTicketsOrder", dataForTicketOrders);
            return new RedirectView(INDEX_PAGE);
        } else {
            logger.info("User entered wrong email or password");
            model.addAttribute("wrongInput", true);
            return new RedirectView(LOGIN_PAGE);
        }
    }
}
