package com.tothenew.sharda.Model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "sellers")
public class Seller {

    @SequenceGenerator(name = "seller_sequence", sequenceName = "seller_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seller_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String gstNumber;
    private String companyContact;
    private String companyName;

    Seller() {

    }

    public Seller(User user, String gstNumber, String companyContact, String companyName) {
        this.user = user;
        this.gstNumber = gstNumber;
        this.companyContact = companyContact;
        this.companyName = companyName;
    }
}