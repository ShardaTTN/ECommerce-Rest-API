package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Dto.Request.ChangePasswordDto;
import com.tothenew.sharda.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PutMapping("/change-password")
    public ResponseEntity<?> changeMyPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        return customerService.changePassword(changePasswordDto);
    }
}