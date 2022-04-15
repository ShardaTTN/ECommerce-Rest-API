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
        //1. check if parent category exists
        if (categoryRepository.existsById(parentCategoryId)) {
            //2. if yes then create a sub category under the provided category
            Category parent = categoryRepository.getById(parentCategoryId);
            Category category = new Category();
            category.setName(categoryName);
            category.setCategory(parent);
            categoryRepository.save(category);
            log.info("sub-category created!");
            return new ResponseEntity<>(String.format("Sub-category created under category: ", parent.getName()), HttpStatus.CREATED);
        } else {
            //3. if not then create a main category
            Category category = new Category();
            category.setName(categoryName);
            categoryRepository.save(category);
            log.info("created main category");
            return new ResponseEntity<>(String.format("Parent category created with id: ", category.getId()), HttpStatus.CREATED);
        }
    }
}