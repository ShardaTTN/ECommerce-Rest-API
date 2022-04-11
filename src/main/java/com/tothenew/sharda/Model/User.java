package com.tothenew.sharda.Model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class User {

    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_sequence")
    private Long id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private Boolean isDeleted;
    private Boolean isActive;
    private Boolean isExpired;
    private Boolean isLocked;
    private Integer invalidAttemptCount;
    private LocalDateTime passwordUpdateDate;
    private String passwordResetToken;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Seller> sellers;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Customer> customers;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Address> address;

    @Lob
    private byte[] image;

}