package com.tothenew.sharda.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;

@Getter
@Setter
@Entity
public class Address {

    @SequenceGenerator(name = "address_sequence", sequenceName = "address_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "address_sequence")
    private Long id;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String addressLine;
    private String label;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "\tCity: "+city+"\n\tState: "+state+"\n\tCountry: "+country+"\n\tAddress Line: "+addressLine+"\n\tZip Code: "+zipcode;
    }
}
