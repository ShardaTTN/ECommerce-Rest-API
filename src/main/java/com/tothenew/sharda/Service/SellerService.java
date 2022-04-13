package com.tothenew.sharda.Service;

import com.tothenew.sharda.Dto.Request.AddAddressDto;
import com.tothenew.sharda.Dto.Request.ChangePasswordDto;
import com.tothenew.sharda.Dto.Request.UpdateCustomerDto;
import com.tothenew.sharda.Dto.Request.UpdateSellerDto;
import com.tothenew.sharda.Exception.TokenExpiredException;
import com.tothenew.sharda.Model.AccessToken;
import com.tothenew.sharda.Model.Address;
import com.tothenew.sharda.Model.Seller;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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

    public ResponseEntity<?> updateSellerProfile(UpdateSellerDto updateSellerDto) {
        String token = updateSellerDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(accessToken.getUser().getEmail())) {
            log.info("User exists.");
            User user = userRepository.findUserByEmail(accessToken.getUser().getEmail());
            user.setFirstName(updateSellerDto.getFirstName());
            user.setMiddleName(updateSellerDto.getMiddleName());
            user.setLastName(updateSellerDto.getLastName());
            user.setEmail(updateSellerDto.getEmail());
            Seller seller = sellerRepository.getSellerByUserId(user.getId());
            seller.setCompanyContact(updateSellerDto.getCompanyContact());
            seller.setCompanyName(updateSellerDto.getCompanyName());
            seller.setGstNumber(updateSellerDto.getGstNumber());
            userRepository.save(user);
            sellerRepository.save(seller);
            log.info("user updated!");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Profile Updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("User profile updated!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update the profile!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> changeSellerPassword(ChangePasswordDto changePasswordDto) {
        String token = changePasswordDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(accessToken.getUser().getEmail())) {
            User user = userRepository.findUserByEmail(accessToken.getUser().getEmail());
            user.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            log.info("Changed password and encoded, then saved it.");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Password Changed");
            mailMessage.setText("ALERT!, Your account's password has been changed, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(user.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Changed Password Successfully!", HttpStatus.OK);
        } else  {
            log.info("Failed to change password!");
            return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSellerAddress(Long id, AddAddressDto addAddressDto) {
        String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
         if (addressRepository.existsById(id)) {
            log.info("address exists");
            Address address = addressRepository.getById(id);
            address.setAddressLine(addAddressDto.getAddress());
            address.setLabel(addAddressDto.getLabel());
            address.setZipcode(addAddressDto.getZipcode());
            address.setCountry(addAddressDto.getCountry());
            address.setState(addAddressDto.getState());
            address.setCity(addAddressDto.getCity());
            log.info("trying to save the updated address");
            addressRepository.save(address);
            return new ResponseEntity<>("Address updated successfully.", HttpStatus.OK);
        }  else if (userRepository.existsByEmail(accessToken.getUser().getEmail())) {
            User user = userRepository.findUserByEmail(accessToken.getUser().getEmail());
            log.info("user exists");
            Address address = new Address();
            address.setUser(user);
            address.setAddressLine(addAddressDto.getAddress());
            address.setCity(addAddressDto.getCity());
            address.setCountry(addAddressDto.getCountry());
            address.setState(addAddressDto.getState());
            address.setZipcode(addAddressDto.getZipcode());
            address.setLabel(addAddressDto.getLabel());
            addressRepository.save(address);
            log.info("Address added to the respected user");
            return new ResponseEntity<>("Added the address.", HttpStatus.CREATED);
        }
         else {
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: "+id), HttpStatus.NOT_FOUND);
        }
    }


}
