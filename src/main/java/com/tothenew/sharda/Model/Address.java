package com.tothenew.sharda.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Address {
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String addressLine;
    private String label;
}
