package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByPasswordResetToken(String token);
    User findUserByEmail(String email);

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



}
