package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.RegistrationConfig.RegistrationService;
import com.tothenew.sharda.Repository.SellerRepository;
import com.tothenew.sharda.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SellerRepository sellerRepository;

    @GetMapping("/admin-board")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping(path = "/seller/confirm")
    public String confirmSeller(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/user-list")
    public @ResponseBody
    List<User> returnUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/customer-list")
    public @ResponseBody List<Object[]> returnCustomers() {
        return userRepository.printPartialDataForCustomers();
    }

    @GetMapping("/seller-list")
    public @ResponseBody List<Object[]> returnSellers() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(userRepository.printPartialDataForSellers());
//        list.addAll(sellerRepository.loadPartialData(1));
        return list;
    }

    @PutMapping("/activate/customer/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable("id") Long id) {
        return registrationService.confirmById(id);
    }
}