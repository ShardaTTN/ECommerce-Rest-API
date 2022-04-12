package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.CustomerService;
import com.tothenew.sharda.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @GetMapping("/seller-profile")
    public ResponseEntity<?> viewSellerProfile(@RequestParam("accessToken") String accessToken) {
        return sellerService.viewSellerProfile(accessToken);
    }
}