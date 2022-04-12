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

}
