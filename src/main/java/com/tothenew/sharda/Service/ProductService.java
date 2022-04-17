package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.Category;
import com.tothenew.sharda.Model.Product;
import com.tothenew.sharda.Model.Seller;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.CategoryRepository;
import com.tothenew.sharda.Repository.ProductRepository;
import com.tothenew.sharda.Repository.SellerRepository;
import com.tothenew.sharda.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;

@Service
@Transactional
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    MailSender mailSender;

    public ResponseEntity<?> addProduct(String name, String brand, Long categoryId, String description, Boolean isCancellable,
                                        Boolean isReturnable, Principal principal) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.getById(categoryId);
            if (category.getCategory() == null) {
                return new ResponseEntity<>("You cannot create a product of Parent Category node!", HttpStatus.BAD_REQUEST);
            } else {
                String email = principal.getName();
                User user = userRepository.findUserByEmail(email);
                log.info("user found"+user.getFirstName());
                Seller seller = sellerRepository.getSellerByUserId(user.getId());
                Product product = new Product();
                product.setCategory(category);
                product.setBrand(brand);
                product.setName(name);
                product.setDescription(description);
                product.setIsActive(false);
                product.setIsCancellable(isCancellable);
                product.setIsReturnable(isReturnable);
                product.setIsDeleted(false);
                product.setSeller(seller);
                product = productRepository.save(product);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("Product Added");
                mailMessage.setText("A new product has been added, It is inactive at the moment. Below are the details of added Product"
                + "\nProduct name: "+product.getName()
                + "\nBrand: "+product.getBrand()
                + "\nCategory: "+product.getCategory().getName()
                + "\nDescription: "+product.getDescription()
                + "\nSeller: "+user.getEmail()
                + "\nActivate the Product.");
                mailMessage.setTo("admin@sd1876.com");
                mailMessage.setFrom("sharda.kumari@tothenew.com");
                Date date = new Date();
                mailMessage.setSentDate(date);
                try {
                    mailSender.send(mailMessage);
                } catch (MailException e) {
                    log.info("Error sending mail");
                }
                return new ResponseEntity<>("Product added to Seller: "+user.getFirstName(), HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("No Category exists with this ID: "+categoryId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteProduct(Long productId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findUserByEmail(email);
        Seller seller = sellerRepository.getSellerByUserId(user.getId());
        if (productRepository.existsById(productId)) {
            Product product = productRepository.getById(productId);
            if (product.getSeller() == seller) {
                productRepository.delete(product);
                log.info("product deleted");
                return new ResponseEntity<>("Product deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This Product does not belong to logged in Seller", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Could not find product associated with this ID: "+productId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateProduct(Long productId, String name,
                                           String description, Boolean isCancellable,
                                           Boolean isReturnable, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findUserByEmail(email);
        Seller seller = sellerRepository.getSellerByUserId(user.getId());
        if (productRepository.existsById(productId)) {
            Product product = productRepository.getById(productId);
            if (product.getSeller() == seller) {
                product.setName(name);
                product.setDescription(description);
                product.setIsCancellable(isCancellable);
                product.setIsReturnable(isReturnable);
                product = productRepository.save(product);
                return new ResponseEntity<>("Updated product, it\' new name is: "+product.getName(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("This Product does not belong to logged in Seller", HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("Could not find product associated with this ID: "+productId, HttpStatus.NOT_FOUND);
        }
    }
}