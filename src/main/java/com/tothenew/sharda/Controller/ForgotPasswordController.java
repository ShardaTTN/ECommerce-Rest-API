package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ForgotPasswordController {

    @Autowired
    UserDetailsServiceImpl userService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {

        String response = userService.forgotPassword(email);

        if (!response.startsWith("Invalid")) {
            response = "http://localhost:8080/reset-password?token=" + response;
        }
        return response;
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password) {

        return userService.resetPassword(token, password);
    }
}
