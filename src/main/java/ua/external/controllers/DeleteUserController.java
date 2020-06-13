package ua.external.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import ua.external.service.interfaces.UserService;
import ua.external.util.PasswordEncryptor;
import ua.external.util.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

import static ua.external.controllers.Paths.DELETE_USER_FILE;
import static ua.external.controllers.Paths.DELETE_USER_PAGE;
import static ua.external.controllers.Paths.INDEX_PAGE;

@Controller
public class DeleteUserController {
    private static final Logger logger = LoggerFactory.getLogger(DeleteUserController.class);

    private UserService<UserDto> userService;

    public DeleteUserController(UserService<UserDto> userService) {
        this.userService = userService;
    }

    @GetMapping(DELETE_USER_PAGE)
    public String getDeleteUser() {
        return DELETE_USER_FILE;
    }

    @PostMapping(DELETE_USER_PAGE)
    public String postDeleteUser(HttpServletRequest request, SessionStatus sessionStatus,
                                 Model model) {
        String email = request.getParameter("email");
        String passwordField1 = request.getParameter("password1");
        String passwordField2 = request.getParameter("password2");

        UserDto user = (UserDto) request.getSession().getAttribute("user");
        boolean isNullPresent = isNullFieldsPresent(email, passwordField1, passwordField2);
        boolean inputPasswordsEquals = passwordField1.equals(passwordField2);
        boolean isDataValid = isInputDataValid(email, passwordField1, user);
        if (isNullPresent || !inputPasswordsEquals || !isDataValid) {
            model.addAttribute("wrongInput", true);
            return DELETE_USER_FILE;
        }

        if (userService.delete(user.getId())) {
            request.getSession().removeAttribute("user");
            request.getSession().removeAttribute("role");
            request.getSession().setAttribute("userInSystem", false);
            request.getSession().invalidate();
            sessionStatus.setComplete();
            return "redirect:" + INDEX_PAGE;
        }
        return DELETE_USER_FILE;
    }

    private boolean isNullFieldsPresent(String email, String passwordField1, String passwordField2) {
        return email == null || passwordField1 == null ||
                passwordField2 == null;
    }

    private boolean isInputDataValid(String email, String inputPassword, UserDto user) {
        String pass;
        try {
            pass = PasswordEncryptor.encrypt(inputPassword);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Something wrong with encryptor", e);
            return false;
        }
        return user.getEmail().equals(email) && pass.equals(user.getPassword());
    }
}
