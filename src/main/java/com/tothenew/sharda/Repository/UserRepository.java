package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByPasswordResetToken(String token);
    User findUserByEmail(String email);

    @Query(value = "SELECT a.is_active from users a WHERE a.id = ?1", nativeQuery = true)
    Boolean isUserActive(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = FALSE WHERE a.email = ?1")
    void disableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.invalidAttemptCount = ?1 WHERE a.email = ?2")
    void updateInvalidAttemptCount(Integer invalidAttemptCount, String email);


    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active FROM users a WHERE a.id = (SELECT user_id from user_roles where role_id = 2)", nativeQuery = true)
    List<Object[]> printPartialDataForCustomers();

    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active, b.company_contact, b.company_name FROM users a, sellers b WHERE a.id = (SELECT user_id from user_roles where role_id = 3) AND b.user_id = (SELECT user_id from user_roles where role_id = 3)", nativeQuery = true)
    List<Object[]> printPartialDataForSellers();
}