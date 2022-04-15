package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName, @RequestParam(required = false, value = "parentId") Long parentCategoryId) {
        return categoryService.addCategory(categoryName, parentCategoryId);
    }

    @PostMapping("/add-categoryMetadata-field")
    public ResponseEntity<?> addCategoryMetadataField(@RequestParam("fieldName") String fieldName) {
        return categoryService.addMetadataField(fieldName);
    }
}
