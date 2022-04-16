package com.tothenew.sharda.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ProductReview {

    @SequenceGenerator(name = "product_review_sequence", sequenceName = "product_review_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "product_review_sequence")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customers_user_id")
    Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    private String review;
    private Double rating;
}