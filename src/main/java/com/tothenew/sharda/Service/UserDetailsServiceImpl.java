package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.PasswordResetToken;
import com.tothenew.sharda.Model.Role;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.RegistrationConfig.Token.ConfirmationToken;
import com.tothenew.sharda.Repository.PasswordResetTokenRepository;
import com.tothenew.sharda.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;


    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with %s email", email)));
        return UserDetailsImpl.build(user);
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public void disableUser(String email) {
        userRepository.disableUser(email);
    }

    public void increaseFailedAttempts(Optional<User> user) {
        int newFailAttempts = user.get().getInvalidAttemptCount()+1;
        userRepository.updateInvalidAttemptCount(newFailAttempts, user.get().getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateInvalidAttemptCount(0, email);
    }

    public void lock(Optional<User> user) {
        user.get().setIsLocked(false);
        userRepository.save(user.get());
    }


    public String resetPassword(String token, String password) {

        Optional<User> userOptional = Optional
                .ofNullable(userRepository.findByPasswordResetToken(token));

        if (!userOptional.isPresent()) {
            return "Invalid token.";
        }

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";

        }

        User user = userOptional.get();

        user.setPassword(password);
        user.setPasswordResetToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);

        return "Your password successfully updated.";
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

}