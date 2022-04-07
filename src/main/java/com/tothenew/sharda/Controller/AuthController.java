package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Dto.LoginDao;
import com.tothenew.sharda.Dto.Request.TokenRefreshRequest;
import com.tothenew.sharda.Dto.Response.JwtResponse;
import com.tothenew.sharda.Dto.Response.MessageResponse;
import com.tothenew.sharda.Dto.Response.TokenRefreshResponse;
import com.tothenew.sharda.Dto.Response.UserInfoResponse;
import com.tothenew.sharda.Dto.SignupCustomerDao;
import com.tothenew.sharda.Dto.SignupSellerDao;
import com.tothenew.sharda.Exception.Email.EmailSender;
import com.tothenew.sharda.Exception.TokenRefreshException;
import com.tothenew.sharda.Model.*;
import com.tothenew.sharda.RegistrationConfig.RegistrationService;
import com.tothenew.sharda.Repository.*;
import com.tothenew.sharda.Security.JwtUtils;
import com.tothenew.sharda.Service.RefreshTokenService;
import com.tothenew.sharda.Service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    EmailSender emailSender;
    @Autowired
    RefreshTokenService refreshTokenService;


    @PostMapping("/customer")
    public ResponseEntity<?> registerAsCustomer(@Valid @RequestBody SignupCustomerDao signupCustomerDao) {
        if (userRepository.existsByEmail(signupCustomerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(signupCustomerDao.getFirstName());
        user.setEmail(signupCustomerDao.getEmail());
        user.setPassword(passwordEncoder.encode(signupCustomerDao.getPassword()));
        user.setLastName(signupCustomerDao.getLastName());
        user.setIsActive(false);
        user.setIsDeleted(false);
        user.setIsExpired(false);
        user.setIsLocked(false);
        user.setInvalidAttemptCount(0);

        Customer customer = new Customer(user, signupCustomerDao.getContact());

        Role roles = roleRepository.findByAuthority("ROLE_CUSTOMER").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        customerRepository.save(customer);

        String token = registrationService.generateToken(user);

        String link = "http://localhost:8080/api/signup/customer/confirm?token="+token;
        emailSender.send(signupCustomerDao.getEmail(), registrationService.buildEmail(signupCustomerDao.getFirstName(), link));
        return new ResponseEntity<>(
                "Customer Registered Successfully!\nHere is your activation token use it with in 3 minutes\n"+token,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/seller")
    public ResponseEntity<?> registerAsSeller(@Valid @RequestBody SignupSellerDao signupSellerDao) {
        if (userRepository.existsByEmail(signupSellerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(signupSellerDao.getFirstName());
        user.setEmail(signupSellerDao.getEmail());
        user.setPassword(passwordEncoder.encode(signupSellerDao.getPassword()));
        user.setLastName(signupSellerDao.getLastName());

        Seller seller = new Seller(user, signupSellerDao.getGstNumber(), signupSellerDao.getCompanyContact(), signupSellerDao.getCompanyName());

        Role roles = roleRepository.findByAuthority("ROLE_SELLER").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        sellerRepository.save(seller);

        String token = registrationService.generateToken(user);

        String link = "http://localhost:8080/api/signup/seller/confirm?token="+token;
        emailSender.send(signupSellerDao.getEmail(), registrationService.buildEmail(signupSellerDao.getFirstName(), link));

        return new ResponseEntity<>(
                "Seller Registered Successfully!\nAsk you Admin to activate your account!",
                HttpStatus.CREATED);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAsAdmin(@Valid @RequestBody LoginDao loginDao){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        String welcomeMessage = "Welcome back, Admin";

        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken());
        return new ResponseEntity<>(welcomeMessage +"\n"+ jwtResponse, HttpStatus.OK);

    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginAsCustomer(@Valid @RequestBody LoginDao loginDao){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);


        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        String welcomeMessage = "Customer logged in Successfully!!";

        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken());
        return new ResponseEntity<>(welcomeMessage +"\n"+ jwtResponse, HttpStatus.OK);

    }

    @PostMapping("/seller/login")
    public ResponseEntity<?> loginAsSeller(@Valid @RequestBody LoginDao loginDao){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDao.getEmail(), loginDao.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        String welcomeMessage = "Seller logged in Successfully!!";

        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken());
        return new ResponseEntity<>(welcomeMessage +"\n"+ jwtResponse, HttpStatus.OK);

    }

//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDao loginRequest) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(
//                      new UserInfoResponse(userDetails.getId(),
//                        userDetails.getEmail(),
//                        roles));
//    }

//    @PostMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(new MessageResponse("You've been signed out!"));
//    }



    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PutMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @PostMapping(path = "/customer/confirm")
    public String confirmByEmail(@RequestParam("email") String email) {
        return registrationService.confirmByEmail(email);
    }


}