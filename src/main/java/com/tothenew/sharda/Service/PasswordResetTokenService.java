package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.PasswordResetToken;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.PasswordResetTokenRepository;
import com.tothenew.sharda.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    MailSender mailSender;

    public ResponseEntity<?> forgotPassword(String email) {
        if (userRepository.existsByEmail(email)) {
            return forgotPasswordUtility(email);
        } else {
            return new ResponseEntity<>("No user exists with this Email ID!", HttpStatus.NOT_FOUND);
        }
    }

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    public ResponseEntity<?> forgotPasswordUtility(String email) {
        User user = userRepository.findUserByEmail(email);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.existsByUserId(user.getId());
        if (passwordResetToken == null) {
            String token = generateToken(user);
            log.info("password reset token generated");


            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
                log.info("mail sent");
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Generated new Password Reset Token, sending to your mailbox", HttpStatus.OK);
        } else {
            passwordResetTokenRepository.deleteById(passwordResetToken.getId());
            log.info("deleted password reset token");
            String token = generateToken(user);
            log.info("password reset token generated");


            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
                log.info("mail sent");
            } catch (MailException e) {
                log.info("Error sending mail");
            }

            return new ResponseEntity<>("Existing Password Reset Token deleted and created new one\n" +
                    "check your mailbox.", HttpStatus.OK);
        }
    }
}