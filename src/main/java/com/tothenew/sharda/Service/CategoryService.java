package com.tothenew.sharda.Service;

import com.tothenew.sharda.Dto.Response.ViewCategoryDto;
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
import java.util.Set;

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

    public ResponseEntity<?> addMetadataField(String fieldName, Long categoryId) {
        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findByCategoryMetadataFieldName(fieldName);
        if (categoryMetadataField != null) {
            return new ResponseEntity<>("You cannot create duplicate category metadata field!", HttpStatus.BAD_REQUEST);
        } else {
            Category category = categoryRepository.getById(categoryId);
            if (category == null) {
                return new ResponseEntity<>("You cannot create metadata fields because no category exists to map!", HttpStatus.NOT_FOUND);
            } else {

                if (category.getCategory() == null) {
                    return new ResponseEntity<>("You cannot create metadata fields for a parent category!", HttpStatus.BAD_REQUEST);
                } else {
                    CategoryMetadataField field = new CategoryMetadataField();
                    field.setName(fieldName);
                    field.setCategory(category);
                    field = categoryMetadataFieldRepository.save(field);
                    log.info("created category metadata field");
                    return new ResponseEntity<>(String.format("Category metadata field created with ID: "+field.getId()), HttpStatus.CREATED);
                }
            }
        }
    }

    public ResponseEntity<?> viewMetadataField() {
        List<CategoryMetadataField> list = categoryMetadataFieldRepository.findAll();
        log.info("returning a list of metadata field.");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    public ResponseEntity<?> addCategoryMetadataFieldValues(Long categoryId, Long metadataFieldId, Set<String> valueList) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.getById(categoryId);
            log.info("category exists");
            if (category.getCategory() == null) {
                return new ResponseEntity<>("You cannot add metadata field values for a parent category!", HttpStatus.BAD_REQUEST);
            } else {
                if (categoryMetadataFieldRepository.existsById(metadataFieldId)) {
                    CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.getById(metadataFieldId);
                    categoryMetadataField.setCategory(category);
                    log.info("category metadata exist");
                    CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
                    categoryMetadataFieldValues.setValueList(valueList);
                    categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
                    categoryMetadataFieldValues.setCategory(category);
                    categoryMetadataFieldRepository.save(categoryMetadataField);
                    categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
                    return new ResponseEntity<>("Added the passed values to category metadata field: "+ categoryMetadataField.getName(), HttpStatus.CREATED);
                } else {
                    log.info("category metadata doesn't exist");
                    return new ResponseEntity<>("No category metadata field exists with this ID: "+metadataFieldId, HttpStatus.NOT_FOUND);
                }
            }
        } else {
            log.info("category does not exists");
            return new ResponseEntity<>("No category exists with this ID: "+categoryId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateCategoryMetadataFieldValues(Long categoryId, Long metadataFieldId, Set<String> valueList) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.getById(categoryId);
            log.info("category exists");
            if (categoryMetadataFieldRepository.existsById(metadataFieldId)) {
                CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.getById(metadataFieldId);
                log.info("category metadata exist");

                //Logic
                CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findByCategoryMetadataFieldId(metadataFieldId);
                categoryMetadataFieldValues.setValueList(valueList);
                categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

                return new ResponseEntity<>("Updated the passed values to category metadata field: "+ categoryMetadataField.getName(), HttpStatus.CREATED);
            } else {
                log.info("category metadata doesn't exist");
                return new ResponseEntity<>("No category metadata field exists with this ID: "+metadataFieldId, HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("category does not exists");
            return new ResponseEntity<>("No category exists with this ID: "+categoryId, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateCategory(Long categoryId, String categoryName) {
        if (categoryRepository.existsById(categoryId)) {
            log.info("category exists");
            Category category = categoryRepository.getById(categoryId);
            Category categoryDuplicate = categoryRepository.findByCategoryName(categoryName);
            if (categoryDuplicate == null) {
                category.setName(categoryName);
                categoryRepository.save(category);
                return new ResponseEntity<>("Saved category with updated name: "+category.getName(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You cannot create duplicate categories!", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No Category exists with name: "+categoryName, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> viewCategory(Long categoryId) {
        Category currCategory = categoryRepository.getById(categoryId);
        List<Category> childCategories = categoryRepository.findChildCategories(categoryId);
        ViewCategoryDto category = new ViewCategoryDto();
        category.setCurrentCategory(currCategory);
        category.setChildCategories(childCategories);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    public ResponseEntity<?> viewAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    public ResponseEntity<?> viewCategoriesByOptionalId(Long id) {
        if (id != null) {
            if (categoryRepository.existsById(id)) {
                List<Category> categories = categoryRepository.findChildCategories(id);
                return new ResponseEntity<>(categories, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Category exists with this id: "+id, HttpStatus.NOT_FOUND);
            }
        } else {
            List<Category> categories = categoryRepository.findCategoryByNull();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }
}