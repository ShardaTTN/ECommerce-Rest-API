package com.tothenew.sharda.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "product_variation")
@Data
public class ProductVariation {

    @SequenceGenerator(name = "product_variation_sequence", sequenceName = "product_variation_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "product_variation_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private String primaryImageName;

    private Boolean isActive;
}