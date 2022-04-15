package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM category a WHERE a.name = ?1", nativeQuery = true)
    Category findByCategoryName(String categoryName);
}