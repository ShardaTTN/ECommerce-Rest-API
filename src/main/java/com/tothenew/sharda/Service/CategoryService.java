package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.Category;
import com.tothenew.sharda.Repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public ResponseEntity<?> addCategory(String categoryName, Long parentCategoryId) {

        if (parentCategoryId != null) {
            if (categoryRepository.existsById(parentCategoryId)) {
                Category parent = categoryRepository.getById(parentCategoryId);
                if (parent.getName().equals(categoryName)) {
                    return new ResponseEntity<>("You cannot create a sub-category of a Parent category", HttpStatus.BAD_REQUEST);
                } else {
                    Category category2 = categoryRepository.findByCategoryName(categoryName);
                    if (category2 != null) {
                        return new ResponseEntity<>("You cannot create duplicate categories!", HttpStatus.BAD_REQUEST);
                    } else {
                        Category category = new Category();
                        category.setName(categoryName);
                        category.setCategory(parent);
                        categoryRepository.save(category);
                        log.info("sub-category created!");
                        return new ResponseEntity<>(String.format("Sub-category created under category: "+parent.getName()), HttpStatus.CREATED);
                    }
                }
            } else {
                return new ResponseEntity<>(String.format("No category exists with this id: "+parentCategoryId), HttpStatus.NOT_FOUND);
            }
        } else {
            Category category2 = categoryRepository.findByCategoryName(categoryName);
            if (category2 != null) {
                return new ResponseEntity<>("You cannot create duplicate categories!", HttpStatus.BAD_REQUEST);
            } else {
                Category category = new Category();
                category.setName(categoryName);
                categoryRepository.save(category);
                log.info("created main category");
                return new ResponseEntity<>(String.format("Parent category created: "+category.getName()), HttpStatus.CREATED);
            }
        }
    }
}