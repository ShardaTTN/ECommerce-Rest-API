package com.tothenew.sharda.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.UserRepository;
import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            if (user.getIsActive() && !user.getIsLocked()) {
                if (user.getInvalidAttemptCount() < UserDetailsServiceImpl.MAX_FAILED_ATTEMPTS - 1) {
                    userDetailsService.increaseFailedAttempts(Optional.of(user));
                } else {
                    userDetailsService.lock(Optional.of(user));
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " Contact Admin to remove lock on your account.");
                }
            }
        }
        logger.error("Unauthorized error: {}", exception.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", exception.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}