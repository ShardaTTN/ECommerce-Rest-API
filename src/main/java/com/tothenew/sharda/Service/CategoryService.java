package com.tothenew.sharda.Service;

import com.tothenew.sharda.Model.Category;
import com.tothenew.sharda.Model.CategoryMetadataField;
import com.tothenew.sharda.Model.CategoryMetadataFieldValues;
import com.tothenew.sharda.Repository.CategoryMetadataFieldRepository;
import com.tothenew.sharda.Repository.CategoryMetadataFieldValuesRepository;
import com.tothenew.sharda.Repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

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
                category = categoryRepository.save(category);
                log.info("created main category");
                return new ResponseEntity<>(String.format("Parent category created with ID: "+category.getId()), HttpStatus.CREATED);
            }
        }
    }

    public ResponseEntity<?> addMetadataField(String fieldName){
        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findByCategoryMetadataFieldName(fieldName);
        if (categoryMetadataField != null) {
            return new ResponseEntity<>("You cannot create duplicate category metadata field!", HttpStatus.BAD_REQUEST);
        } else {
            CategoryMetadataField field = new CategoryMetadataField();
            field.setName(fieldName);
            field = categoryMetadataFieldRepository.save(field);
            log.info("created category metadata field");
            return new ResponseEntity<>(String.format("Category metadata field created with ID: "+field.getId()), HttpStatus.CREATED);
        }

    }

    public ResponseEntity<?> addCategoryMetadataFieldValues(Long categoryId, Long metadataFieldId, List<String> valueList) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.getById(categoryId);
            log.info("category exists");
            if (categoryMetadataFieldRepository.existsById(metadataFieldId)) {
                CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.getById(metadataFieldId);
                log.info("category metadata exist");

                //Logic
                CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
                categoryMetadataFieldValues.setValueList(valueList);
                categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
                categoryMetadataFieldValues.setCategory(category);
                categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

                return new ResponseEntity<>("Added the passed values to category metadata field: "+ categoryMetadataField.getName(), HttpStatus.CREATED);
            } else {
                log.info("category metadata doesn't exist");
                return new ResponseEntity<>("No category metadata field exists with this ID: "+metadataFieldId, HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("category does not exists");
            return new ResponseEntity<>("No category exists with this ID: "+categoryId, HttpStatus.NOT_FOUND);
        }
    }
}