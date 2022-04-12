package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "SELECT a.city, a.state, a.country, a.zipcode, a.address_line, a.label from address a WHERE a.user_id = ?1", nativeQuery = true)
    List<Object[]> findByUserId(Long id);
}