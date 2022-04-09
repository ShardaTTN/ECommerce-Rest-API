package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.AccessToken;
import com.tothenew.sharda.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    @Autowired
    UserRepository userRepository;


}