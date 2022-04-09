package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT a.contact from Customers a WHERE a.user_id = ?1", nativeQuery = true)
    String getContactOfUserId(Long id);
}