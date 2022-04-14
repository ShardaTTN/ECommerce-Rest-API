package com.tothenew.sharda.Repository;

import com.tothenew.sharda.Model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query(value = "SELECT * FROM password_reset_token a WHERE a.user_id = ?1", nativeQuery = true)
    PasswordResetToken existsByUserId(Long id);


}