package com.tothenew.sharda.Model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity(name = "refreshtoken")
public class RefreshToken {
    @SequenceGenerator(name = "refresh_token_sequence", sequenceName = "refresh_token_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_sequence")
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;
}
