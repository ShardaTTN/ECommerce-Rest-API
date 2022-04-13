package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Dto.Request.AddAddressDto;
import com.tothenew.sharda.Dto.Request.ChangePasswordDto;
import com.tothenew.sharda.Dto.Request.UpdateCustomerDto;
import com.tothenew.sharda.Dto.Request.UpdateSellerDto;
import com.tothenew.sharda.Service.CustomerService;
import com.tothenew.sharda.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PutMapping("/update-seller-profile")
    public ResponseEntity<?> updateSellerProfile(@Valid @RequestBody UpdateSellerDto updateSellerDto) {
        return sellerService.updateSellerProfile(updateSellerDto);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeSellerPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        return sellerService.changeSellerPassword(changePasswordDto);
    }

    @PutMapping("/update-seller-address")
    public ResponseEntity<?> updateSellerAddress(@RequestParam("addressId") Long id, @RequestBody AddAddressDto addAddressDto) {
        return sellerService.updateSellerAddress(id, addAddressDto);
    }


}