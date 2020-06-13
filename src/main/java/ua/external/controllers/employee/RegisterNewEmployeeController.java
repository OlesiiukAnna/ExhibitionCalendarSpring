package ua.external.controllers.employee;

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

import static ua.external.controllers.Paths.REGISTER_NEW_EMPLOYEE_FILE;
import static ua.external.controllers.Paths.REGISTER_NEW_EMPLOYEE_PAGE;

@Controller
public class RegisterNewEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterNewEmployeeController.class);

    private UserService<UserDto> userService;

    public RegisterNewEmployeeController(UserService<UserDto> userService) {
        this.userService = userService;
    }

    @GetMapping(REGISTER_NEW_EMPLOYEE_PAGE)
    public String getRegisterNewEmployee() {
        return REGISTER_NEW_EMPLOYEE_FILE;
    }

    @PostMapping(REGISTER_NEW_EMPLOYEE_PAGE)
    public String postRegisterNewEmployee(@RequestParam Map<String, String> allParams, Model model) {
        UserDto userDto = new UserDto();
        String email = allParams.get("employee-email");
        String password = allParams.get("employee-password");
        String firstName = allParams.get("employee-first-name");
        String lastName = allParams.get("employee-last-name");
        String phone = allParams.get("employee-phone");
        String role = allParams.get("employee-role");
        if (email == null || password == null ||
                firstName == null || lastName == null ||
                phone == null) {
            model.addAttribute("isInvalidData", true);
            return REGISTER_NEW_EMPLOYEE_FILE;
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
            model.addAttribute("isEmployeeExists", true);
            logger.warn("Such user is already exists", e);
        } catch (InvalidUserException e) {
            model.addAttribute("isInvalidData", true);
            logger.warn("Invalid input data", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("PasswordEncryptor missed algorithm", e);
        }

        if (isSaved) {
            model.addAttribute("employeeRegistered", true);
        }
        return REGISTER_NEW_EMPLOYEE_FILE;
    }
}
