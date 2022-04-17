package com.tothenew.sharda.Controller;

import com.tothenew.sharda.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<?> addCategoryMetadataField(@RequestParam("fieldName") String fieldName, @RequestParam("categoryId") Long categoryId) {
        return categoryService.addMetadataField(fieldName, categoryId);
    }

    @PostMapping("/add-category-metadata-field-values")
    public ResponseEntity<?> addCategoryMetadataFieldValues(@RequestParam("categoryId") Long categoryId, @RequestParam("metadataFieldId") Long metadataFieldId, @RequestParam("values") List<String> valueList) {
        return categoryService.addCategoryMetadataFieldValues(categoryId, metadataFieldId, valueList);
    }

    @GetMapping("/metadata-field")
    public ResponseEntity<?> viewMetadataField(){
        return categoryService.viewMetadataField();
    }

    @PutMapping("/update-category-metadata-field-values")
    public ResponseEntity<?> updateCategoryMetadataFieldValues(@RequestParam("categoryId") Long categoryId, @RequestParam("metadataFieldId") Long metadataFieldId, @RequestParam("values") List<String> valueList) {
        return categoryService.updateCategoryMetadataFieldValues(categoryId, metadataFieldId, valueList);
    }

    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long categoryId, @RequestParam("categoryName") String categoryName) {
        return categoryService.updateCategory(categoryId, categoryName);
    }

    @GetMapping("/view-category")
    public ResponseEntity<?> viewCategory(@RequestParam("categoryId") Long categoryId) {
        return categoryService.viewCategory(categoryId);
    }

    @GetMapping("/list-categories")
    public ResponseEntity<?> viewAllCategories() {
        return categoryService.viewAllCategories();
    }

    @GetMapping("/list-all-categories")
    public ResponseEntity<?> listCategories(@RequestParam(required = false, value = "categoryId") Long id) {
        return categoryService.viewCategoriesByOptionalId(id);
    }
}
