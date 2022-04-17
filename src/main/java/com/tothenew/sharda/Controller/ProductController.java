package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestParam("name") String name,
                                        @RequestParam("brand") String brand,
                                        @RequestParam("categoryId") Long categoryId,
                                        @RequestParam(required = false, value = "description") String description,
                                        @RequestParam(required = false, value = "isCancellable", defaultValue = "false") Boolean isCancellable,
                                        @RequestParam(required = false, value = "isReturnable", defaultValue = "false") Boolean isReturnable,
                                        Principal principal) {
        return productService.addProduct(name, brand, categoryId, description, isCancellable, isReturnable, principal);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId, Principal principal) {
        return productService.deleteProduct(productId, principal);
    }

    @PutMapping("/update-product")
    public ResponseEntity<?> updateProduct(@RequestParam("productId") Long productId,
                                           @RequestParam(required = false, value = "name") String name,
                                           @RequestParam(required = false, value = "description") String description,
                                           @RequestParam(required = false, value = "isCancellable") Boolean isCancellable,
                                           @RequestParam(required = false, value = "isReturnable") Boolean isReturnable,
                                           Principal principal) {
        return productService.updateProduct(productId, name, description, isCancellable, isReturnable, principal);
    }
}