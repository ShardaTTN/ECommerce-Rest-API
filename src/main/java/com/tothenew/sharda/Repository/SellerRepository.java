package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.Customer;
import com.tothenew.sharda.Model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional(readOnly = true)
public interface SellerRepository extends JpaRepository<Seller, Long> {

    @Query(value = "SELECT a.company_contact from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getCompanyContactOfUserId(Long id);

    @Query(value = "SELECT a.company_name from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getCompanyNameOfUserId(Long id);

    @Query(value = "SELECT a.gst_number from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    String getGstNumberOfUserId(Long id);

    @Query(value = "SELECT * from sellers a WHERE a.user_id = ?1", nativeQuery = true)
    Seller getSellerByUserId(Long id);
}