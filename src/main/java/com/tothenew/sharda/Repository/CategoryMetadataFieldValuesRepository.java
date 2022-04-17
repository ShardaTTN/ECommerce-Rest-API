package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, Long> {

    @Query(value = "SELECT * FROM category_metadata_field_values a WHERE a.category_metadata_field_id = ?1", nativeQuery = true)
    CategoryMetadataFieldValues findByCategoryMetadataFieldId(Long id);
}