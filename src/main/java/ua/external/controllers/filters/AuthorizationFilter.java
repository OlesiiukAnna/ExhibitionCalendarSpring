package ua.external.controllers.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.external.util.dto.UserDto;
import ua.external.util.enums.Role;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.external.controllers.Paths.ERROR_FILE;

public class AuthorizationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) resp;
        final HttpSession session = request.getSession();
        String servletPath = request.getServletPath();

        UserDto userDto = (UserDto) session.getAttribute("user");
        if (servletPath.contains("/employee") && !userDto.getRole().equals(Role.EMPLOYEE)) {
            response.sendRedirect(ERROR_FILE);
        } else if (servletPath.contains("/visitor") && !userDto.getRole().equals(Role.VISITOR)) {
            response.sendRedirect(ERROR_FILE);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

