package com.tothenew.sharda.Repository;

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

    @Query(value = "SELECT a.company_contact, a.company_name from Sellers a WHERE a.user_id = (select ?1 from users)", nativeQuery = true)
    List<Object[]> loadPartialData(Long id);

}