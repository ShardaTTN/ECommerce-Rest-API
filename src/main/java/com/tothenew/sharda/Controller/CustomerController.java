package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/my-profile")
    public ResponseEntity<?> viewMyProfile(@RequestParam("accessToken") String accessToken) {
        return customerService.viewMyProfile(accessToken);
    }
}