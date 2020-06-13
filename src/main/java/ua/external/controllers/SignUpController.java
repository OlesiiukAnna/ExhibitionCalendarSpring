package ua.external.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.external.exceptions.user.InvalidUserException;
import ua.external.exceptions.user.SuchUserIsAlreadyExistsException;
import ua.external.service.interfaces.UserService;
import ua.external.util.dto.UserDto;
import ua.external.util.enums.Role;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static ua.external.controllers.Paths.LOGIN_PAGE;
import static ua.external.controllers.Paths.SIGN_UP_FILE;
import static ua.external.controllers.Paths.SIGN_UP_PAGE;

@Controller
public class SignUpController {
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    private UserService<UserDto> userService;

    public SignUpController(UserService<UserDto> userService) {
        this.userService = userService;
    }

    @GetMapping(SIGN_UP_PAGE)
    public String getSignUp() {
        return SIGN_UP_FILE;
    }

    @PostMapping(SIGN_UP_PAGE)
    public String postSignUp(@RequestParam Map<String, String> allParams, Model model) {
        UserDto userDto = new UserDto();
        String email = allParams.get("email");
        String password = allParams.get("password");
        String firstName = allParams.get("first-name");
        String lastName = allParams.get("last-name");
        String phone = allParams.get("phone");
        String role = allParams.get("role");
        if (email == null || password == null ||
                firstName == null || lastName == null ||
                phone == null) {
            return SIGN_UP_PAGE;
        } else {
            userDto.setEmail(email);
            userDto.setPassword(password);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setPhone(phone);
            userDto.setRole(Role.valueOf(role));
        }
        boolean isSaved = false;
        try {
            isSaved = userService.save(userDto);
        } catch (SuchUserIsAlreadyExistsException e) {
            model.addAttribute("isUserExists", true);
            logger.warn("Such user is already exists", e);
        } catch (InvalidUserException e) {
            model.addAttribute("isInvalidData", true);
            logger.warn("Invalid input data", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String result;
        if (isSaved) {
            result = "redirect:" + LOGIN_PAGE;
        } else {
            result = SIGN_UP_FILE;
        }
        return result;
    }
}
