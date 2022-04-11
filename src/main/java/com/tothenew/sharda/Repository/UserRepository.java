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

    @Query(value = "SELECT a.is_active from Users a WHERE a.id = ?1", nativeQuery = true)
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


    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active FROM Users a WHERE a.id = (SELECT user_id from USER_ROLES where role_id = 2)", nativeQuery = true)
    List<Object[]> printPartialDataForCustomers();

    @Query(value = "SELECT a.id, a.first_name, a.last_name, a.email, a.is_active FROM Users a WHERE a.id = (SELECT user_id from USER_ROLES where role_id = 3)", nativeQuery = true)
    List<Object[]> printPartialDataForSellers();

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String email);

}



