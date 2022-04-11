package com.tothenew.sharda.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.UserRepository;
import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserRepository userRepository;

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
            } else if (!user.isAccountNonLocked()) {
                if (userDetailsService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }
        }

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