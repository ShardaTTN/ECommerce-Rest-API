package com.tothenew.sharda.Service;

import com.tothenew.sharda.Exception.TokenExpiredException;
import com.tothenew.sharda.Model.AccessToken;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.AccessTokenRepository;
import com.tothenew.sharda.Repository.CustomerRepository;
import com.tothenew.sharda.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class CustomerService {

    @Autowired
    AccessTokenRepository accessTokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> viewMyProfile(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        User user = userRepository.findUserByEmail(token.getUser().getEmail());
        return new ResponseEntity<>("Customer User Id: "+user.getId()+"\nCustomer First name: "+user.getFirstName()+"\nCustomer Last name: "+user.getLastName()+"\nCustomer active status: "+user.getIsActive()+"\nCustomer contact: "+customerRepository.getContactOfUserId(user.getId()), HttpStatus.OK);
    }
}