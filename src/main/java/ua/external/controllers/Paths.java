package ua.external.controllers;

public interface Paths {
    String INDEX_FILE = "/index";
    String INDEX_PAGE = "/";

    String ERROR_FILE = "/WEB-INF/view/error.jsp";
    String ERROR_PAGE = "/";

    String LOGIN_FILE = "login";
    String LOGIN_PAGE = "/login";

    String SIGN_UP_FILE = "signup";
    String SIGN_UP_PAGE = "/signup";

    String LOGOUT_PAGE = "/logout";

    String DELETE_USER_FILE = "delete-user";
    String DELETE_USER_PAGE = "/delete-user";

    String VISITOR_HOME_PAGE_FILE = "visitor/visitor-home-page";
    String VISITOR_HOME_PAGE_PAGE = "/visitor-home-page";

    String EMPLOYEE_HOME_PAGE_FILE = "employee/employee-home-page";
    String EMPLOYEE_HOME_PAGE_PAGE = "/employee-home-page";

    String EXHIBITIONS_FILE = "exhibitions";
    String EXHIBITIONS_PAGE = "/exhibitions";

    String EXHIBITION_HALLS_FILE = "exhibition-halls";
    String EXHIBITION_HALLS_PAGE = "/exhibition-halls";

    String EXHIBITION_HALL_FILE = "exhibition-hall";
    String EXHIBITION_HALL_PAGE = "/exhibition-hall";

    String REGISTER_NEW_EMPLOYEE_FILE = "employee/register-new-employee";
    String REGISTER_NEW_EMPLOYEE_PAGE = "/employee/register-new-employee";

    String REGISTER_NEW_EXHIBITION_HALL_FILE = "employee/register-new-exhibition-hall";
    String REGISTER_NEW_EXHIBITION_HALL_PAGE = "/employee/register-new-exhibition-hall";

    String REGISTER_NEW_EXHIBITION_FILE = "employee/register-new-exhibition";
    String REGISTER_NEW_EXHIBITION_PAGE = "/employee/register-new-exhibition";

    String CONFIRM_TICKETS_FILE = "employee/tickets";
    String CONFIRM_TICKETS_PAGE = "/employee/tickets";

    String CART_FILE = "cart";
    String CART_PAGE = "/cart";
    String CART_DELETE_TICKET_ACTION = "/cart-delete-ticket";
    String CART_SAVE_ACTION = "/cart-save";
}
