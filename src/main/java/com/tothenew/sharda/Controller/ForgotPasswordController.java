package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.PasswordResetTokenService;
import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/user")
public class ForgotPasswordController {

    @Autowired
    UserDetailsServiceImpl userService;
    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Email @RequestParam String email) {
        return passwordResetTokenService.forgotPassword(email);
    }

    @PatchMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password) {

        return userService.resetPassword(token, password);
    }
}
