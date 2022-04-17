package com.tothenew.sharda.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Data
public class PasswordResetToken {

    @SequenceGenerator(name = "password_reset_token_sequence", sequenceName = "password_reset_token_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_sequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordResetToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}