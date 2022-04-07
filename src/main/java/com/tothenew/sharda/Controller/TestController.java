package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Model.Customer;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.RegistrationConfig.RegistrationService;
import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content";
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content" ;
    }

    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public String moderatorAccess() {
        return "Seller Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping(path = "/seller/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public String confirmSeller(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/admin/customerlist")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> returnCustomers() {
        return userDetailsService.getAllUsers();
    }
}