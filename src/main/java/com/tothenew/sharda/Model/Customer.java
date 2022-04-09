package com.tothenew.sharda.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "customer_sequence")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String contact;

    Customer() {

    }

    public Customer(User user, String contact) {
        this.user = user;
        this.contact = contact;
    }
}