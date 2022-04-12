package com.tothenew.sharda.Service;

import com.tothenew.sharda.Exception.TokenExpiredException;
import com.tothenew.sharda.Model.AccessToken;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SellerService {

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MailSender mailSender;
    @Autowired
    AddressRepository addressRepository;

    public ResponseEntity<?> viewSellerProfile(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(token.getUser().getEmail())) {
            log.info("User exists!");
            User user = userRepository.findUserByEmail(token.getUser().getEmail());
            List<Object[]> list = addressRepository.findByUserId(user.getId());
            log.info("returning a list of objects.");
            return new ResponseEntity<>("Seller User Id: "+user.getId()+"\nSeller First name: "+user.getFirstName()+"\nSeller Last name: "+user.getLastName()+"\nSeller active status: "+user.getIsActive()+"\nSeller companyContact: "+sellerRepository.getCompanyContactOfUserId(user.getId())+"\nSeller companyName: "+sellerRepository.getCompanyNameOfUserId(user.getId())+"\nSeller gstNumber: "+sellerRepository.getGstNumberOfUserId(user.getId())+"\nAddress: "+list, HttpStatus.OK);
        } else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }

    }
}
